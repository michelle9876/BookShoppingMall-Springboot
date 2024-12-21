package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCardDTO {
    private String cardNumber;
    private Integer expirationMonth;
    private Integer expirationYear;
    private Integer securityNumber;

}
