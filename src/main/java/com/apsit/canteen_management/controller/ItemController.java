package com.apsit.canteen_management.controller;

import com.apsit.canteen_management.dto.ItemDto;
import com.apsit.canteen_management.dto.SaveItemDto;
import com.apsit.canteen_management.entity.MenuItem;
import com.apsit.canteen_management.enums.ItemCategory;
import com.apsit.canteen_management.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    // Instead of using @ModelAttribute we can manually use @RequestParam for each parameter in the request
    public ResponseEntity<MenuItem> saveItem(@ModelAttribute SaveItemDto saveItemDto) {
        return itemService.saveItem(saveItemDto);
    }
    @PostMapping("/save/all")
    public ResponseEntity<List<MenuItem>> saveListOfItem(@RequestBody List<MenuItem> menuItems){
        return itemService.saveListOfItem(menuItems);
    }
    @GetMapping
    public List<ItemDto> getAllItem(){
        return itemService.getAllItem();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemDto> toggleAvailability(@PathVariable Long id){
        return itemService.toggleAvailability(id);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ItemDto>> findByPriceBetween(@RequestParam int minPrice, @RequestParam int highPrice){
        return itemService.findByPriceBetween(minPrice, highPrice);
    }

}
