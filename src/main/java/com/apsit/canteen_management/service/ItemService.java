package com.apsit.canteen_management.service;

import com.apsit.canteen_management.dto.ItemDto;
import com.apsit.canteen_management.entity.Item;
import com.apsit.canteen_management.enums.ItemCategory;
import com.apsit.canteen_management.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<Item> saveItem(Item item){
        return ResponseEntity.ok(itemRepository.save(item));
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
                .map(item-> modelMapper.map(item, ItemDto.class))
                .map(ResponseEntity:: ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<ItemDto>> getItemsByCategory(ItemCategory itemCategory){
        return itemRepository.findAllByCategory(itemCategory)
                .map(items -> items.stream()
                        .map(item->modelMapper.map(item, ItemDto.class))
                        .toList())
                .map(ResponseEntity:: ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    public ResponseEntity<ItemDto> toggleAvailability(Long id){
        return itemRepository.findById(id)
                .map(item-> {
                    item.setAvailable(!item.isAvailable());
                    return ResponseEntity.ok(modelMapper.map(itemRepository.save(item), ItemDto.class));
                })
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<ItemDto>> findByPriceBetween(int minPrice, int highPrice){
        return itemRepository.findByPriceBetween(minPrice, highPrice)
                .map(items-> items.stream()
                        .map(item->modelMapper.map(item, ItemDto.class))
                        .toList())
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

}
