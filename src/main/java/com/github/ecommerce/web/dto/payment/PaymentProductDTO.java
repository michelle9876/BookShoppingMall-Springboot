package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProductDTO {
    private Integer paymentProductId;
    private Integer quantity;
    private Float totalPrice;
}
