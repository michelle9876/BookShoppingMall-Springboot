package com.github.ecommerce.web.dto.mypage;

import com.github.ecommerce.data.entity.payment.Payment;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailDTO {

    private Integer userId;
    private Integer paymentId;
    private String paymentCard;
    private String zipCode;
    private String mainAddress;
    private String detailsAddress;

    private Integer totalPrice;
    private String receiverName;
    private String receiverPhone;
    private LocalDateTime paymentDate;
    private  LocalDateTime expectedDelivery;

    private List<PaymentProductDTO> paymentProducts;


    public static PaymentDetailDTO from(Payment item, Integer userId) {
        return new PaymentDetailDTO(
                userId,
                item.getPaymentId(),
                item.getPaymentCard(),
                item.getZipCode(),
                item.getMainAddress(),
                item.getDetailsAddress(),
                Math.round(item.getTotalPrice()),
                item.getReceiverName(),
                item.getReceiverPhone(),
                item.getPaymentDate(),
                item.getExpectedDelivery(),
                item.getPaymentProducts().stream()
                        .map(PaymentProductDTO::from)
                        .toList()
        );
    }

}



