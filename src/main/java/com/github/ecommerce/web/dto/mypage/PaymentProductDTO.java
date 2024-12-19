package com.github.ecommerce.web.dto.mypage;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentProductDTO {

    private Integer paymentProductId;

    private Integer quantity;
    private Integer eachTotalPrice; //책 결제 전체 가격

    private Integer bookId;
    private String title;
    private Integer eachPrice; //책가격
    private String bookImage;
    private String publisher;
    private String author;

}
