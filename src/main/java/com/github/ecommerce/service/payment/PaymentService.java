package com.github.ecommerce.service.payment;

import com.github.ecommerce.data.entity.auth.User;
import com.github.ecommerce.data.entity.cart.Cart;
import com.github.ecommerce.data.entity.payment.Payment;
import com.github.ecommerce.data.repository.book.BookRepository;
import com.github.ecommerce.data.repository.cart.CartRepository;
import com.github.ecommerce.data.repository.payment.PaymentProductRepository;
import com.github.ecommerce.data.repository.payment.PaymentRepository;
import com.github.ecommerce.web.dto.payment.BookInfoForPaymentPage;
import com.github.ecommerce.web.dto.payment.PaymentRequestDTO;
import com.github.ecommerce.web.dto.payment.PaymentResponseDTO;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
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
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final IamPortService iamPortService;

    @Transactional

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

    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) throws IamportResponseException, IOException {
        // 1. 카트 아이템 가져오기
        List<Cart> cartItems = cartRepository.findAllByUserId(paymentRequestDTO.getUserId());

        // 2. 책 정보 및 총 금액 계산
//       **  Stream can be changed to using For Loop : to perform faster
        List<BookInfoForPaymentPage> finalResult = cartItems.stream().map(cart -> {
            BookInfoForPaymentPage bookInfo = new BookInfoForPaymentPage();
            bookInfo.setTitle(cart.getBook().getTitle());
            bookInfo.setQuantity(cart.getQuantity());
            bookInfo.setTotal(cart.getQuantity() * cart.getBook().getPrice());
            return bookInfo;
        }).toList();


        double total = finalResult.stream()
                .mapToDouble(BookInfoForPaymentPage::getTotal)
                .sum();

        // 3. 결제 검증 (모의 구현)
        boolean verified = iamPortService.verifyPayment(paymentRequestDTO.getImpUid(), new BigDecimal(total));
        if (!verified) {
            throw new IllegalArgumentException("Payment verification failed");
        }

        // 4. 재고 감소 및 카트 삭제
        cartItems.forEach(cart -> {
            cart.getBook().setStockQuantity(cart.getBook().getStockQuantity() - cart.getQuantity());
            bookRepository.save(cart.getBook()); // 재고 변경 사항 저장
        });
        cartRepository.deleteAll(cartItems); // 카트 비우기

        // 5. 결제 정보 저장
        Payment payment = new Payment();
        payment.setUser(new User(paymentRequestDTO.getUserId())); // 사용자 정보 설정
        payment.setImpUid(Integer.valueOf(paymentRequestDTO.getImpUid())); // 결제 고유 ID
        payment.setPaymentCard(paymentRequestDTO.getPaymentCard()); // 결제 카드 정보
        payment.setZipCode(paymentRequestDTO.getZipCode()); // 배송 우편번호
        payment.setMainAddress(paymentRequestDTO.getMainAddress()); // 배송 메인 주소
        payment.setDetailsAddress(paymentRequestDTO.getDetailsAddress()); // 배송 상세 주소
        payment.setReceiverName(paymentRequestDTO.getReceiverName()); // 수령자 이름
        payment.setReceiverPhone(paymentRequestDTO.getReceiverPhone()); // 수령자 전화번호
        payment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now())); // 결제 시간 설정
        payment.setExpectedDelivery(Timestamp.valueOf(LocalDateTime.now().plusDays(7))); // 예상 배송 시간 설정
        payment.setTotalPrice((float)total); // 총 금액 설정

        Payment savedPayment = paymentRepository.save(payment); // 결제 정보 저장

        // 6. 결제 응답 반환
        return new PaymentResponseDTO(
                savedPayment.getPaymentId(), // 결제 ID
                savedPayment.getTotalPrice(), // 총 금액
                savedPayment.getPaymentDate(), // 결제 일시
                savedPayment.getExpectedDelivery() // 예상 배송 일시
        );

    }

}
