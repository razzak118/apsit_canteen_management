package com.apsit.canteen_management.controller;

import com.apsit.canteen_management.dto.ItemDto;
import com.apsit.canteen_management.entity.MenuItem;
import com.apsit.canteen_management.enums.ItemCategory;
import com.apsit.canteen_management.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public ResponseEntity<MenuItem> saveItem(@RequestBody MenuItem menuItem){
        return itemService.saveItem(menuItem);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteItemById(@PathVariable Long id){
        return itemService.deleteItem(id);
    }

    @GetMapping("/{itemName}")
    public ResponseEntity<ItemDto> getItemByItemName(@PathVariable String itemName){
        return itemService.getItemByItemName(itemName);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<ItemDto>> getItemsByCategory(@PathVariable ItemCategory categoryName){
        return itemService.getItemsByCategory(categoryName);
    }

    @PatchMapping("/{id}/toggleAvailability")
    public ResponseEntity<ItemDto> toggleAvailability(@PathVariable Long id){
        return itemService.toggleAvailability(id);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ItemDto>> findByPriceBetween(@RequestParam int minPrice, @RequestParam int highPrice){
        return itemService.findByPriceBetween(minPrice, highPrice);
    }

}
