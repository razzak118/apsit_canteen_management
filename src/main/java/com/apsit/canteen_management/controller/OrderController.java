package com.apsit.canteen_management.controller;

import com.apsit.canteen_management.dto.OrderTicketDto;
import com.apsit.canteen_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/place")
    public ResponseEntity<OrderTicketDto> placeOrder(){
        return orderService.placeOrder();
    }
}
