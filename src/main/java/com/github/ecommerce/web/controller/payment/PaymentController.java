package com.github.ecommerce.web.controller.payment;

import com.github.ecommerce.service.payment.PaymentService;
import com.github.ecommerce.web.dto.payment.PaymentRequestDTO;
import com.github.ecommerce.web.dto.payment.PaymentResponseDTO;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) throws IamportResponseException, IOException {
        PaymentResponseDTO response = paymentService.processPayment(paymentRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        if (payments.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(payments);
        }
    }

    @GetMapping("/all/user")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByUserId(@RequestParam Integer userId) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByUserId(userId);
        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(payments);
        }
    }

    @GetMapping
    public ResponseEntity<Page<PaymentResponseDTO>> getPageOfPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Page<PaymentResponseDTO> payments = paymentService.getPageOfPayments(page, size);
        if (payments.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(payments);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Integer id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
