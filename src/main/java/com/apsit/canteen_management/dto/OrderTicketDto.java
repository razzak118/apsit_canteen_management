package com.apsit.canteen_management.dto;

import com.apsit.canteen_management.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderTicketDto {
    private String username;
    private List<OrderItemDto> orderItems;
    private double totalAmount;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;
}
