package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private Integer userId;
    private String impUid;
    private String merchantUid;
    private String paymentCard;
    private String zipCode;
    private String mainAddress;
    private String detailsAddress;
    private String receiverName;
    private String receiverPhone;

}
