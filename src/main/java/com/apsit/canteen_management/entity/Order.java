package com.apsit.canteen_management.entity;

import com.apsit.canteen_management.enums.OrderStatus;
import jakarta.persistence.*;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
