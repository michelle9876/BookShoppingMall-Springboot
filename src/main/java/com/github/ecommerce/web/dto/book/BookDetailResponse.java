package com.github.ecommerce.web.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDetailResponse {

    private Integer bookId;
    private String bookTitle;
    private String bookImageUrl;
    private int bookPrice;
    private String bookSummary;
    private String author;
    private String publisher;

    @NotNull
    @Min(value = 1)
    private Integer amount;

}
