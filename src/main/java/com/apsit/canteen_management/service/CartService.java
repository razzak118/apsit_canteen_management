package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.CartDto;
import com.apsit.canteen_management.entity.Cart;
import com.apsit.canteen_management.entity.CartItem;
import com.apsit.canteen_management.entity.MenuItem;
import com.apsit.canteen_management.entity.User;
import com.apsit.canteen_management.repository.CartRepository;
import com.apsit.canteen_management.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;
    private User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public ResponseEntity<CartDto> getCartById(){
        User user=getUser();
        return cartRepository.findById(user.getUserId())
                .map(cart -> modelMapper.map(cart, CartDto.class))
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }
    @Transactional
    public ResponseEntity<CartDto> addItemToCart(Long itemId){
        User user=getUser();
        Cart prevCart=cartRepository.findById(user.getUserId()).orElseThrow();
        MenuItem menuItem= itemRepository.findById(itemId).orElseThrow();
        double cartCurrentTotal= prevCart.getTotalCartPrice()==null? 0.0 : prevCart.getTotalCartPrice();
        Iterator<CartItem> iterator=prevCart.getCartItems().iterator();
        while(iterator.hasNext()){
            CartItem cartItem=iterator.next();
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
    @Transactional
    public ResponseEntity<CartDto> adjustQuantity(Long cartItemId, int change){
        User user=getUser();
        Cart prevCart=cartRepository.findById(user.getUserId()).orElseThrow(()->new RuntimeException("Your has been deleted! ask admin."));
        prevCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getCartItemId().equals(cartItemId))
                .findFirst()
                .ifPresent(itemToChange->{
                    int newQty=itemToChange.getQuantity()+change;
                    if(newQty<=0){
                        prevCart.getCartItems().remove(itemToChange);
                    }else{
                        itemToChange.setQuantity(newQty);
                        itemToChange.setCartItemPrice(itemToChange.getMenuItem().getPrice()*newQty);
                    }
                });
        double cartTotal=prevCart.getCartItems().stream()
                .mapToDouble(items->items.getCartItemPrice())
                .sum();
        prevCart.setTotalCartPrice(cartTotal);
        return ResponseEntity.ok(modelMapper.map(cartRepository.save(prevCart), CartDto.class));
    }

    @Transactional
    public ResponseEntity<CartDto> removeItemCompletelyFromCart(Long cartItemId) {
        User user=getUser();
        Cart prevcart=cartRepository.findById(user.getUserId()).orElseThrow();
        Double currentCartPrice=prevcart.getTotalCartPrice();
        prevcart.getCartItems().stream()
                .filter(cartItem -> cartItem.getCartItemId().equals(cartItemId))
                .findFirst()
                .ifPresent(itemToRemove->{
                    prevcart.setTotalCartPrice(currentCartPrice-itemToRemove.getCartItemPrice());
                    prevcart.getCartItems().remove(itemToRemove);
                });
        return ResponseEntity.ok(modelMapper.map(cartRepository.save(prevcart), CartDto.class));

    }
    public void clearCart(){
        User user=getUser();
        Cart cart=cartRepository.findById(user.getUserId()).orElseThrow();
        cart.getCartItems().clear();
        cart.setTotalCartPrice(0.0);
    }
}
