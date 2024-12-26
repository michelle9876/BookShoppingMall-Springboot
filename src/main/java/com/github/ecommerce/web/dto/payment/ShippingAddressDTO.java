package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressDTO {
    private String mainAddress;
    private String detailsAddress;
    private String zipCode;
}
