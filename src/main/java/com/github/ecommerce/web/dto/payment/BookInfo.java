package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookInfo {
    private int bookId;
    private String title;
    private double price;
    private int stockQuantity;

}
