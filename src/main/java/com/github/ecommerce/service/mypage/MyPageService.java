package com.github.ecommerce.service.mypage;

import com.github.ecommerce.data.entity.auth.User;
import com.github.ecommerce.data.entity.cart.Cart;
import com.github.ecommerce.data.entity.payment.Payment;
import com.github.ecommerce.data.repository.cart.CartRepository;
import com.github.ecommerce.data.repository.mypage.UserRepository;
import com.github.ecommerce.data.repository.payment.PaymentRepository;
import com.github.ecommerce.service.exception.AccessDeniedException;
import com.github.ecommerce.service.exception.NotFoundException;
import com.github.ecommerce.service.exception.QuantityExceededException;
import com.github.ecommerce.service.exception.S3Exception;
import com.github.ecommerce.service.s3Image.AwsS3Service;
import com.github.ecommerce.web.dto.mypage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    //user repository
    private final UserRepository userRepository;
    // s3 service
    private final AwsS3Service awsS3Service;

    //cart repository
    private final CartRepository cartRepository;
    //payment repository
    private final PaymentRepository paymentRepository;

    // 사용자 찾기 메서드
    private User findUserByIdOrThrow(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MyPageStatus.USER_NOT_FOUNDED.getMessage()));
    }

    //유저 정보 가지고오기
    public UserInfoDTO getUserInfo(Integer userId) {
        User user = findUserByIdOrThrow(userId);
        return UserInfoDTO.from(user);
    }

    //유저 정보 수정하기
    @Transactional(transactionManager = "tmJpa1")
    public UserInfoDTO putUserInfo(Integer userId, UserInfoDTO userInfo, String newImageUrl) {
        User user = findUserByIdOrThrow(userId);

        user.updateProfileImage(newImageUrl);
        user.updateUserName(userInfo.getUserName());
        user.updatePhone(userInfo.getPhone());
        user.updateGender(userInfo.getGender());
        user.updateZipCode(userInfo.getZipCode());
        user.updateMainAddress(userInfo.getMainAddress());
        user.updateDetailAddress(userInfo.getDetailsAddress());

        return UserInfoDTO.from(user);
    }

    // 기존 프로필 이미지 url 가져오기
    public String getPreviousImageUrl(Integer userId) throws S3Exception {
        User user = findUserByIdOrThrow(userId);
        return user.getProfileImage();
    }

    // S3에 프로필 이미지 업로드
    public String uploadProfileImage(MultipartFile image) throws S3Exception {
        return awsS3Service.upload(image);
    }

    // S3에서 프로필 이미지 삭제
    public void deleteProfileImage(String imageUrl) throws S3Exception {
        awsS3Service.deleteImageFromS3(imageUrl);
    }



    //장바구니 목록 가지고오기
    public CartListDTO getCartItems(Integer userId) {
        List<Cart> cartList = cartRepository.findAllByUserId(userId);
        List<CartDetailDTO> items = cartList.stream().map(
                //Cart -> CartDetailDTO
                item -> CartDetailDTO.builder()
                        .userId(item.getUser().getUserId())
                        .cartId(item.getCartId())
                        .bookId(item.getBook().getBookId())
                        .title(item.getBook().getTitle())
                        .price((int) item.getBook().getPrice())
                        .author(item.getBook().getAuthor())
                        .publisher(item.getBook().getPublisher())
                        .bookImage(item.getBook().getBookImageUrl())
                        .quantity(item.getQuantity())
                        .stockQuantity(item.getBook().getStockQuantity())
                        .build()
        ).toList();

        CartListDTO result = new CartListDTO();
        result.setCartItems(items);
        result.setStatus(new DefaultDTO(MyPageStatus.CART_ITEMS_RETURN));
        return result;
    }

    //장바구니 상세
    public CartListDTO getCartItem(Integer userId, String id) {
        Integer cartId = Integer.valueOf(id);
        Cart item = cartRepository.findByIdFetchJoin(cartId).orElse(null);
        if(item == null) {
            return new CartListDTO(MyPageStatus.CART_NOT_FOUNDED);
        }else if(!item.getUser().getUserId().equals(userId)) {
            return new CartListDTO(MyPageStatus.USER_ERROR_FORBIDDEN);
        }
        CartDetailDTO cart = CartDetailDTO.builder()
                .userId(item.getUser().getUserId())
                .cartId(item.getCartId())
                .bookId(item.getBook().getBookId())
                .title(item.getBook().getTitle())
                .price((int) item.getBook().getPrice())
                .author(item.getBook().getAuthor())
                .publisher(item.getBook().getPublisher())
                .bookImage(item.getBook().getBookImageUrl())
                .quantity(item.getQuantity())
                .stockQuantity(item.getBook().getStockQuantity())
                .build();


        List<CartDetailDTO> carts = new ArrayList<>();
        carts.add(cart);
        CartListDTO result = new CartListDTO();
        result.setStatus(new DefaultDTO(MyPageStatus.CART_RETURN));
        result.setCartItems(carts);

        return result;
    }

    //장바구니 옵션 수정
    @Transactional(transactionManager = "tmJpa1")
    public DefaultDTO putCartOption(Integer userId, CartDetailDTO cartDetailDTO) {
        Cart cartItem = cartRepository.findByIdFetchJoin(cartDetailDTO.getCartId()).orElse(null);
        if(cartItem == null) {
            return new DefaultDTO(MyPageStatus.CART_ERROR);
        }else if(!cartItem.getUser().getUserId().equals(userId)){
            return new DefaultDTO(MyPageStatus.USER_ERROR_FORBIDDEN);
        }else if(cartItem.getBook().getStockQuantity() < cartDetailDTO.getQuantity()){
            return new DefaultDTO(MyPageStatus.CART_QUANTITY_ERROR);
        }
        cartItem.setQuantity(cartDetailDTO.getQuantity());
        return new DefaultDTO(MyPageStatus.CART_PUT);
    }

    //장바구니 삭제
    @Transactional(transactionManager = "tmJpa1")
    public DefaultDTO deleteCartItems(Integer userId, List<CartDetailDTO>  cartDetailDTOs) {

        // 각 항목에 대해 삭제 처리
        for (CartDetailDTO cartDetailDTO : cartDetailDTOs) {
            Cart cartItem = cartRepository.findById(cartDetailDTO.getCartId()).orElse(null);

            if(cartItem == null) {
                return new DefaultDTO(MyPageStatus.CART_ERROR);
            }else if(!cartItem.getUser().getUserId().equals(userId)){
                return new DefaultDTO(MyPageStatus.CART_ID_ACCESS_ERROR);
            }
            cartRepository.delete(cartItem);
        }

        return new DefaultDTO(MyPageStatus.CART_DELETE);
    }



    //결제 내역 목록
    public PaymentListDTO getPaymentList(Integer userId) {
        List<Payment> paymentList = paymentRepository.findAllByUser_UserId(userId);

        List<PaymentDetailDTO> itmes = paymentList.stream().map(
                //Payment -> PaymentDetailDTO
                item -> PaymentDetailDTO.builder()
                        .userId(userId)
                        .paymentId(item.getPaymentId())
                        .paymentCard(item.getPaymentCard())
                        .zipCode(item.getZipCode())
                        .mainAddress(item.getMainAddress())
                        .detailsAddress(item.getDetailsAddress())
                        .totalPrice(Math.round(item.getTotalPrice()) )
                        .receiverName(item.getReceiverName())
                        .receiverPhone(item.getReceiverPhone())
                        .paymentDate(item.getPaymentDate())
                        .expectedDelivery(item.getExpectedDelivery())
                        .paymentProducts(item.getPaymentProducts().stream().map(
                                product -> PaymentProductDTO.builder()
                                        .paymentProductId(product.getPaymentProductId())
                                        .bookId(product.getBook().getBookId())
                                        .title(product.getBook().getTitle())
                                        .publisher(product.getBook().getPublisher())
                                        .bookImage(product.getBook().getBookImageUrl())
                                        .author(product.getBook().getAuthor())
                                        .eachPrice((int) product.getBook().getPrice())
                                        .eachTotalPrice(Math.round(product.getTotalPrice()))
                                        .quantity(product.getQuantity())
                                        .build()
                        ).toList())
                        .build()
        ).toList();

        PaymentListDTO result = new PaymentListDTO();
        result.setPayments(itmes);
        result.setStatus(new DefaultDTO(MyPageStatus.PAYMENT_LIST_RETURN));
        return result;
    }

    //결제내역 상세
    public PaymentListDTO getPaymentDetail(Integer userId, String id) {
        Integer paymentId = Integer.valueOf(id);

        Payment payment = paymentRepository.findByIdJoinPaymentProduct(paymentId).orElse(null);
        if(payment == null) {
            return new PaymentListDTO(MyPageStatus.PAYMENT_NOT_FOUNDED);
        }

        PaymentDetailDTO detailInfo = PaymentDetailDTO.builder()
                .userId(userId)
                .paymentId(payment.getPaymentId())
                .paymentCard(payment.getPaymentCard())
                .zipCode(payment.getZipCode())
                .mainAddress(payment.getMainAddress())
                .detailsAddress(payment.getDetailsAddress())
                .totalPrice(Math.round(payment.getTotalPrice()) )
                .receiverName(payment.getReceiverName())
                .receiverPhone(payment.getReceiverPhone())
                .paymentDate(payment.getPaymentDate())
                .expectedDelivery(payment.getExpectedDelivery())
                .paymentProducts(payment.getPaymentProducts().stream().map(
                        product -> PaymentProductDTO.builder()
                                .paymentProductId(product.getPaymentProductId())
                                .bookId(product.getBook().getBookId())
                                .title(product.getBook().getTitle())
                                .publisher(product.getBook().getPublisher())
                                .bookImage(product.getBook().getBookImageUrl())
                                .author(product.getBook().getAuthor())
                                .eachPrice((int) product.getBook().getPrice())
                                .eachTotalPrice(Math.round(product.getTotalPrice()))
                                .quantity(product.getQuantity())
                                .build()
                ).toList())
                .build();


        List<PaymentDetailDTO> payments = new ArrayList<>();
        payments.add(detailInfo);

        PaymentListDTO result = new PaymentListDTO();
        result.setStatus(new DefaultDTO(MyPageStatus.PAYMENT_RETURN));
        result.setPayments(payments);

        return result;
    }

}
