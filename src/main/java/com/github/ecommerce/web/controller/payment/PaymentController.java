package com.github.ecommerce.web.controller.payment;

import com.github.ecommerce.service.payment.PaymentService;
import com.github.ecommerce.web.dto.payment.PaymentInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

   @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentInfoDTO paymentInfo) {
        // Temporary- Hardcoded : user ID
        int userId = 1;

        // Validate credit card number using Luhn algorithm
       if (!paymentService.validateCreditCard(paymentInfo.getPaymentCard().getCardNumber())){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid credit card number");
       }

       // Validate shipping address
       if (!paymentService.validateAddress(paymentInfo.getShippingAddress())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid shipping address");

       }
       // Process the payment using the service and calculate total price
        float totalPrice = paymentService.processOrder(paymentInfo, userId);

//        paymentService.savePaymentInfo(paymentInfo);
        return ResponseEntity.status(HttpStatus.OK).body("Payment processed successfully. Total price: " + totalPrice);
   }
}
