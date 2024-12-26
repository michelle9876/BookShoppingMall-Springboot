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
    public Page<CartDetailDTO> getCartItems(Integer userId, Pageable pageable) {
        // 카트 아이템을 페이지네이션하여 가져오기
        Page<Cart> cartItems = cartRepository.findAllByUserId(userId, pageable);

        // Cart -> CartDetailDTO 변환 및 페이지 반환
        return cartItems.map(CartDetailDTO::from); // Cart -> CartDetailDTO 변환

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
        return CartDetailDTO.from(item);
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
    public Page<PaymentDetailDTO>  getPaymentList(Integer userId, Pageable pageable) {
        // 카트 아이템을 페이지네이션하여 가져오기
        Page<Payment> paymentList = paymentRepository.findAllByUser_UserId(userId, pageable);
        // Payment -> PaymentDetailDTO 변환 및 페이지 반환
        return paymentList.map(item -> PaymentDetailDTO.from(item, userId));
    }

    //결제내역 상세
    public PaymentListDTO getPaymentDetail(Integer userId, String id) {
        Integer paymentId = Integer.valueOf(id);

        Payment payment = paymentRepository.findByIdJoinPaymentProduct(paymentId).orElse(null);
        if(payment == null) {
            return new PaymentListDTO(MyPageStatus.PAYMENT_NOT_FOUNDED);
        }

        return PaymentDetailDTO.from( payment, userId);
    }

}
