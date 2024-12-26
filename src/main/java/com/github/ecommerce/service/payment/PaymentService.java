package com.github.ecommerce.service.payment;

import com.github.ecommerce.data.entity.auth.User;
import com.github.ecommerce.data.entity.cart.Cart;
import com.github.ecommerce.data.entity.payment.Payment;
import com.github.ecommerce.data.repository.cart.CartRepository;
import com.github.ecommerce.data.repository.mypage.UserRepository;
import com.github.ecommerce.data.repository.payment.PaymentProductRepository;
import com.github.ecommerce.data.repository.payment.PaymentRepository;
import com.github.ecommerce.service.book.BookService;
import com.github.ecommerce.web.dto.payment.PaymentRequestDTO;
import com.github.ecommerce.web.dto.payment.PaymentResponseDTO;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentProductRepository paymentProductRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final BookService bookService;
    private final IamPortService iamPortService;

    public List<PaymentResponseDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll(); // 모든 결제 정보 가져오기
        return payments.stream()
                .map(payment -> new PaymentResponseDTO(
                        payment.getPaymentId(),
                        payment.getTotalPrice(),
                        payment.getPaymentDate(),
                        payment.getExpectedDelivery()
                ))
                .toList();

//                .collect(Collectors.toList()); // PaymentResponseDTO 리스트로 변환
    }


    public Optional<PaymentResponseDTO> getPaymentById(Integer id) {
        Optional<Payment> payment = paymentRepository.findById(id); // ID로 결제 정보 검색
        return payment.map(p -> new PaymentResponseDTO(
                p.getPaymentId(),
                p.getTotalPrice(),
                p.getPaymentDate(),
                p.getExpectedDelivery()
        )); // 결제 정보를 DTO로 변환하여 반환
    }

    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) throws IamportResponseException, IOException {
        // 1. 카트 아이템 가져오기
        final List<Cart> cartItems = cartRepository.findAllByUserId(paymentRequestDTO.getUserId());

        // 2. 책 정보 및 총 금액 계산
        float total = getTotalPrice(cartItems);

        // 3. 결제 검증 (모의 구현)
        this.verifyIamPortPayment(paymentRequestDTO, total);

        // 4. 재고 감소 및 카트 삭제
        bookService.reduceBookStocks(cartItems);
        cartRepository.deleteAll(cartItems); // 카트 비우기

        // 5. 결제 정보 저장
        final Payment savedPayment = saveNewPayment(paymentRequestDTO, total);

        // 6. 결제 응답 반환
        return new PaymentResponseDTO(
                savedPayment.getPaymentId(), // 결제 ID
                savedPayment.getTotalPrice(), // 총 금액
                savedPayment.getPaymentDate(), // 결제 일시
                savedPayment.getExpectedDelivery() // 예상 배송 일시
        );
    }

    private void verifyIamPortPayment(PaymentRequestDTO paymentRequestDTO, float total) throws IamportResponseException, IOException {
        if (!iamPortService.verifyPayment(paymentRequestDTO.getImpUid(), new BigDecimal(total))) {
            throw new IllegalArgumentException("Payment verification failed");
        }
    }

    private static float getTotalPrice(List<Cart> cartItems) {
        float total = 0;
        for (Cart cartItem : cartItems) {
            float totalBookPrice = cartItem.getQuantity() * cartItem.getBook().getPrice();
            total += totalBookPrice;
        }
        return total;
    }

    private Payment saveNewPayment(PaymentRequestDTO paymentRequestDTO, float total) {
        final Payment payment = this.newPaymentFromData(paymentRequestDTO, total);

        // 결제 정보 저장
        return paymentRepository.save(payment);
    }

    private Payment newPaymentFromData(PaymentRequestDTO paymentRequestDTO, float total) {
//        final User user = new User(paymentRequestDTO.getUserId());
        final User user = userRepository.findById(paymentRequestDTO.getUserId()).orElseThrow();
        final LocalDateTime now = LocalDateTime.now();
        return new Payment(paymentRequestDTO.getImpUid(), // 결제 고유 ID
                paymentRequestDTO.getMerchantUid(),
                user, // 사용자 정보 설정
                paymentRequestDTO.getPaymentCard(), // 결제 카드 정보
                paymentRequestDTO.getZipCode(), // 배송 우편번호
                paymentRequestDTO.getMainAddress(), // 배송 메인 주소
                paymentRequestDTO.getDetailsAddress(), // 배송 상세 주소
                total, // 총 금액 설정
                paymentRequestDTO.getReceiverName(), // 수령자 이름
                paymentRequestDTO.getReceiverPhone(), // 수령자 전화번호
                now, // 결제 시간 설정
                now.plusDays(7)); // 예상 배송 시간 설정
    }

    public Page<PaymentResponseDTO> getPageOfPayments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return paymentRepository.findAll(pageable).map(payment -> new PaymentResponseDTO(
                payment.getPaymentId(),
                payment.getTotalPrice(),
                payment.getPaymentDate(),
                payment.getExpectedDelivery()
        ));
    }
}
