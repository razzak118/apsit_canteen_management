package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.OrderTicketDto;
import com.apsit.canteen_management.entity.Cart;
import com.apsit.canteen_management.entity.OrderItem;
import com.apsit.canteen_management.entity.OrderTicket;
import com.apsit.canteen_management.entity.User;
import com.apsit.canteen_management.enums.OrderStatus;
import com.apsit.canteen_management.repository.CartRepository;
import com.apsit.canteen_management.repository.OrderTicketRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderTicketRepository orderTicketRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<OrderTicketDto> placeOrder(){
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart=cartRepository.findById(user.getUserId()).orElseThrow();
        if(cart.getCartItems()==null || cart.getCartItems().isEmpty()){
            throw new IllegalArgumentException("Can't place order! your cart is empty.\ntry finding something you like!");
        }
        OrderTicket orderTicket= OrderTicket.builder()
                .username(user.getUsername())
                .totalAmount(cart.getTotalCartPrice())
                .createdAt(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .build();

        List<OrderItem> orderItems= cart.getCartItems().stream()
                .map(cartItem ->
                    OrderItem.builder()
                            .orderTicket(orderTicket)
                            .menuItem(cartItem.getMenuItem())
                            .quantity(cartItem.getQuantity())
                            .historicalPrice(cartItem.getCartItemPrice()/ cartItem.getQuantity())
                            .build()
                ).toList();
        orderTicket.setOrderItems(orderItems);
        OrderTicket placedOrder= orderTicketRepository.save(orderTicket);
        cart.getCartItems().clear();
        cart.setTotalCartPrice(0.0);
        cartRepository.save(cart);
        return ResponseEntity.ok(modelMapper.map(placedOrder, OrderTicketDto.class));
    }
}
