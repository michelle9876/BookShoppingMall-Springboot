package com.github.ecommerce.data.entity.payment;

import com.github.ecommerce.data.entity.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
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


    @ManyToOne(fetch = FetchType.LAZY)
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
    private Timestamp paymentDate;

    @Column(name = "expected_delivery")
    private Timestamp expectedDelivery;




    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    private List<PaymentProduct> paymentProducts; // 여러 PaymentProduct를 참조
}