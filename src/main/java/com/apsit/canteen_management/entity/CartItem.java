package com.apsit.canteen_management.entity;

import com.apsit.canteen_management.dto.ItemDto;
import jakarta.persistence.*;

@Entity
public class CartItem {
    @Id
    @Column(name = "cart_id")
    private Long cartId;
    @ManyToOne
    @MapsId
    @JoinColumn(name="cart_id")
    private Cart cart;
    private Long menuItem;
    private Integer quantity;
}
