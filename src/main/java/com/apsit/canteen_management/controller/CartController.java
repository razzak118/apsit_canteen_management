package com.apsit.canteen_management.controller;

import com.apsit.canteen_management.dto.CartDto;
import com.apsit.canteen_management.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping()
    public ResponseEntity<CartDto> getCartById(){
        return cartService.getCartById();
    }

    @PostMapping("/add/{itemId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable Long itemId){
        return cartService.addItemToCart(itemId);
    }

    @PostMapping("/remove/{itemId}")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long itemId){
        return cartService.removeItemFromCart(itemId);
    }

}
