package com.github.ecommerce.web.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private Integer cartId;
    private Integer userId;
    private Integer bookId;
    private String title;
    private Integer quantity;
    private int price;
}