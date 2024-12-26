package com.github.ecommerce.data.repository.payment;

import com.github.ecommerce.data.entity.payment.PaymentProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentProductRepository extends JpaRepository<PaymentProduct, Integer> {


}
