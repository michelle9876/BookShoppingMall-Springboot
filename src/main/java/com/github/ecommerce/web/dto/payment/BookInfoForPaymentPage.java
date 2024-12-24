package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoForPaymentPage {
        private String title;
        private Integer quantity;
        private float total;
}


