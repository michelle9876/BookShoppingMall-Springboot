package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProductResponseDTO {
    private String bookTitle;
    private float price;
    private int quantity;
    private float totalPrice;
}
