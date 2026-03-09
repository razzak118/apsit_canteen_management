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
    @GetMapping("/my-cart")
    public ResponseEntity<CartDto> getCartById(){
        return cartService.getCartById();
    }

    @PostMapping("/addToCart/{itemId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable Long itemId){
        return cartService.addItemToCart(itemId);
    }

    @PostMapping("/qty/update")
    public ResponseEntity<CartDto> updateQty(@RequestParam Long cartItemId,@RequestParam int change){
        return cartService.adjustQuantity(cartItemId, change);
    }

    @PostMapping("/deleteItemfromCart/{itemId}")
    public ResponseEntity<CartDto> removeItemCompletelyFromCart(@PathVariable Long itemId){
        return cartService.removeItemCompletelyFromCart(itemId);
    }
}
