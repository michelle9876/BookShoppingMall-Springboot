package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoDTO {
    private PaymentCardDTO paymentCard;
    private ShippingAddressDTO shippingAddress;
    private List<BookDTO> books;

}
