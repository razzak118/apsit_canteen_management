package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.CartDto;
import com.apsit.canteen_management.entity.Cart;
import com.apsit.canteen_management.entity.CartItem;
import com.apsit.canteen_management.entity.MenuItem;
import com.apsit.canteen_management.entity.User;
import com.apsit.canteen_management.repository.CartRepository;
import com.apsit.canteen_management.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;

    public ResponseEntity<CartDto> getCartById(){
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cartRepository.findById(user.getUserId())
                .map(cart -> modelMapper.map(cart, CartDto.class))
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    public ResponseEntity<CartDto> addItemToCart(Long itemId){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart prevCart=cartRepository.findById(user.getUserId()).orElseThrow();
        MenuItem menuItem= itemRepository.findById(itemId).orElseThrow();
        double cartCurrentTotal= prevCart.getTotalCartPrice()==null? 0.0 : prevCart.getTotalCartPrice();
        for(CartItem cartItem: prevCart.getCartItems()){
            if(cartItem.getMenuItem().getItemId().equals(menuItem.getItemId())){
                cartItem.setQuantity(cartItem.getQuantity()+1);
                cartItem.setCartItemPrice(cartItem.getCartItemPrice()+menuItem.getPrice());
                prevCart.setTotalCartPrice(cartCurrentTotal+menuItem.getPrice());
                return ResponseEntity.ok(modelMapper.map(cartRepository.save(prevCart), CartDto.class));
            }
        }
        CartItem cartItem=CartItem.builder()
                .cartItemPrice(menuItem.getPrice())
                .cart(prevCart)
                .quantity(1)
                .cartItemImageUrl(menuItem.getImageUrl())
                .menuItem(menuItem)
                .build();
        prevCart.addItemToCart(cartItem);
        prevCart.setTotalCartPrice(cartCurrentTotal+menuItem.getPrice());
        return ResponseEntity.ok(modelMapper.map(cartRepository.save(prevCart), CartDto.class));
    }

    public ResponseEntity<CartDto> removeItemFromCart(Long itemId){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart prevCart=cartRepository.findById(user.getUserId()).orElseThrow();
        MenuItem menuItem= itemRepository.findById(itemId).orElseThrow();
        double cartCurrentTotal= prevCart.getTotalCartPrice()==null? 0.0 : prevCart.getTotalCartPrice();
        for(CartItem cartItem: prevCart.getCartItems()){
            if(cartItem.getMenuItem().getItemId().equals(menuItem.getItemId())){
                prevCart.setTotalCartPrice(cartCurrentTotal-menuItem.getPrice());
                cartItem.setQuantity(cartItem.getQuantity()-1);
                cartItem.setCartItemPrice(cartItem.getCartItemPrice()-menuItem.getPrice());
                break;
            }
        }
        return ResponseEntity.ok(modelMapper.map(cartRepository.save(prevCart), CartDto.class));
    }

}
