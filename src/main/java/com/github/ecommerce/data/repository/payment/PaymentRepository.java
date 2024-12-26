package com.github.ecommerce.data.repository.payment;

import com.github.ecommerce.data.entity.payment.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
//    Page<Payment> findPayments(Pageable pageable);

//    @Query("SELECT p From Payment p JOIN FETCH p.paymentProducts pp WHERE p.user.userId = :userId")
//    List<Payment> findAllByUser_UserId(@Param("userId") Integer userId);

//    @Query("SELECT p From Payment p JOIN FETCH p.paymentProducts pp WHERE p.paymentId = :paymentId")
//    Optional<Payment> findByIdJoinPaymentProduct(@Param("paymentId") Integer paymentId);
}
