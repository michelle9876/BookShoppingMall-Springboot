package com.github.ecommerce.web.dto.book;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookResponse {
    private Integer id;
    private String bookImageUrl;
    private String bookTitle;
    private int bookPrice;

    public BookResponse(Integer id, String bookImageUrl, String bookTitle, int bookPrice) {
        this.id = id;
        this.bookImageUrl = bookImageUrl;
        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
    }
}
