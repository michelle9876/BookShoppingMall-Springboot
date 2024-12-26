package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Integer  bookId;
    private String bookImage;
    private String bookTitle;
    private float bookPrice;
    private Integer quantity;
}
