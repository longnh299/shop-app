package com.example.shopbe.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "fullname")
    private String fullName;

    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    private String address;

    private String note;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(columnDefinition = "ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled')")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "total_money")
    private Float totalMoney;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "payment_method")
    private String paymentMethod;

    private boolean active;

}

