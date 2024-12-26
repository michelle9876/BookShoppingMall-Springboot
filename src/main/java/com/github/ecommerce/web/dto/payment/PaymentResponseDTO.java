package com.github.ecommerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Integer paymentId;

    private String userName;
    private String phone;
    private String mainAddress;
    private String detailAddress;
    private String zipCode;

    private List<BookDTO> books;
//    private String bookImage;
//    private String bookTitle;
//    private float bookPrice;

    private float totalPrice;
    private LocalDateTime paymentDate;
    private LocalDateTime expectedDelivery;

//    public PaymentResponseDTO(Integer paymentId, String userName, String phone, String mainAddress, String detailAddress, String zipCode,  float totalPrice, LocalDateTime paymentDate, LocalDateTime expectedDelivery) {
//        this.paymentId = paymentId;
//        this.userName = userName;
//        this.phone = phone;
//        this.mainAddress = mainAddress;
//        this.detailAddress = detailAddress;
//        this.zipCode = zipCode;
////        this.books = books;
//        this.totalPrice = totalPrice;
//        this.paymentDate = paymentDate;
//        this.expectedDelivery = expectedDelivery;
//    }
}
