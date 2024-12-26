package com.github.ecommerce.data.entity.payment;

import com.github.ecommerce.data.entity.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @Column(name = "imp_uid")
    private String impUid;

    @Column(name = "merchant_uid")
    private String merchantUid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "payment_card")
    private String paymentCard;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "main_address")
    private String mainAddress;

    @Column(name = "details_address")
    private String detailsAddress;

    @Column(name = "total_price")
    private Float totalPrice;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_phone")
    private String receiverPhone;


    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "expected_delivery")
    private LocalDateTime expectedDelivery;


    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PaymentProduct> paymentProducts = new ArrayList<>();; // 여러 PaymentProduct를 참조

    public Payment(String impUid, String merchantUid, User user, String paymentCard, String zipCode, String mainAddress, String detailsAddress, Float totalPrice, String receiverName, String receiverPhone, LocalDateTime paymentDate, LocalDateTime expectedDelivery) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.user = user;
        this.paymentCard = paymentCard;
        this.zipCode = zipCode;
        this.mainAddress = mainAddress;
        this.detailsAddress = detailsAddress;
        this.totalPrice = totalPrice;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.paymentDate = paymentDate;
        this.expectedDelivery = expectedDelivery;
    }
}