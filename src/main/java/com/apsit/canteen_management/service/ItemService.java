package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.ItemDto;
import com.apsit.canteen_management.entity.MenuItem;
import com.apsit.canteen_management.enums.ItemCategory;
import com.apsit.canteen_management.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<MenuItem> saveItem(MenuItem menuItem){
        return ResponseEntity.ok(itemRepository.save(menuItem));
    }

    public ResponseEntity deleteItem(Long id){
        try{
            itemRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            throw(new IllegalArgumentException("Id doesn't exist !!"));
        }
    }

    public ResponseEntity<ItemDto> getItemByItemName(String name){
        return itemRepository.findByItemNameIgnoreCase(name)
                .map(menuItem -> modelMapper.map(menuItem, ItemDto.class))
                .map(ResponseEntity:: ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<ItemDto>> getItemsByCategory(ItemCategory itemCategory){
        return itemRepository.findAllByCategory(itemCategory)
                .map(items -> items.stream()
                        .map(menuItem ->modelMapper.map(menuItem, ItemDto.class))
                        .toList())
                .map(ResponseEntity:: ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    public ResponseEntity<ItemDto> toggleAvailability(Long id){
        return itemRepository.findById(id)
                .map(menuItem -> {
                    menuItem.setAvailable(!menuItem.isAvailable());
                    return ResponseEntity.ok(modelMapper.map(itemRepository.save(menuItem), ItemDto.class));
                })
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<ItemDto>> findByPriceBetween(int minPrice, int highPrice){
        return itemRepository.findByPriceBetween(minPrice, highPrice)
                .map(items-> items.stream()
                        .map(menuItem ->modelMapper.map(menuItem, ItemDto.class))
                        .toList())
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

}
