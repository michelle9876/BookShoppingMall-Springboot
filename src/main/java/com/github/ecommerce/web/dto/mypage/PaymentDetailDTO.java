package com.github.ecommerce.web.dto.mypage;

import com.github.ecommerce.data.entity.cart.Cart;
import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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
    private Timestamp paymentDate;
    private Timestamp expectedDelivery;

    private List<PaymentProductDTO> paymentProducts;

}



