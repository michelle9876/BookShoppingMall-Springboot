package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Integer paymentId;
    private Character paymentCard;
    private String zipCode;
    private String mainAddress;
    private String detailsAddress;
    private Float totalPrice;
    private Timestamp paymentDate;
    private Timestamp expectedDelivery;

}
