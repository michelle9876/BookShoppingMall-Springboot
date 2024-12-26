package com.github.ecommerce.web.dto.mypage;


import com.github.ecommerce.data.entity.payment.PaymentProduct;
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

    public static PaymentProductDTO from(PaymentProduct product) {
        return PaymentProductDTO.builder()
                .paymentProductId(product.getPaymentProductId())
                .bookId(product.getBook().getBookId())
                .title(product.getBook().getTitle())
                .publisher(product.getBook().getPublisher())
                .bookImage(product.getBook().getBookImageUrl())
                .author(product.getBook().getAuthor())
                .eachPrice((int) product.getBook().getPrice())
                .eachTotalPrice(Math.round(product.getTotalPrice()))
                .quantity(product.getQuantity())
                .build();
    }

}
