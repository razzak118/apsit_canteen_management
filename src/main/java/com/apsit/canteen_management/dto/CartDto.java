package com.apsit.canteen_management.dto;

import com.apsit.canteen_management.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long cartId;
    private List<CartItemDto> cartItems;
    private Integer totalCartPrice;
}
