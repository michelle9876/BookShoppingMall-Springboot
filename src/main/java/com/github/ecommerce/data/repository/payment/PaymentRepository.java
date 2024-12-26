package com.github.ecommerce.data.repository.payment;

import com.github.ecommerce.data.entity.payment.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
  //    Page<Payment> findPayments(Pageable pageable);

  @Query("SELECT p FROM Payment p JOIN FETCH p.user u JOIN FETCH p.paymentProducts pp WHERE u.userId = :userId ORDER BY p.paymentId DESC")
  Page<Payment> findAllByUser_UserId(@Param("userId") Integer userId, Pageable pageable);

  @Query("SELECT p From Payment p JOIN FETCH p.paymentProducts pp WHERE p.paymentId = :paymentId ORDER BY p.paymentId, pp.paymentProductId")
  Optional<Payment> findByIdJoinPaymentProduct(@Param("paymentId") Integer paymentId);

  @Query("SELECT p FROM Payment p JOIN FETCH p.paymentProducts pp JOIN FETCH pp.book b WHERE p.user.userId = :userId")
  List<Payment> findByUserId(@Param("userId") Integer userId);

}
