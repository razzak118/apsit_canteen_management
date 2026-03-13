package com.apsit.canteen_management.controller;

import com.apsit.canteen_management.dto.ItemDto;
import com.apsit.canteen_management.dto.SaveItemDto;
import com.apsit.canteen_management.entity.MenuItem;
import com.apsit.canteen_management.enums.ItemCategory;
import com.apsit.canteen_management.repository.ItemRepository;
import com.apsit.canteen_management.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping
    public ResponseEntity<Page<ItemDto>> getAllItem(@RequestParam(required = false, defaultValue = "0") int pageNo){
        return itemService.getAllItem(pageNo);
    }

    @GetMapping("/{itemName}")
    public ResponseEntity<ItemDto> getItemByItemName(@PathVariable String itemName){
        return itemService.getItemByItemName(itemName);
    }

    @GetMapping("/category")
    public ResponseEntity<Page<ItemDto>> getItemsByCategory(
                            @RequestParam ItemCategory categoryName,
                            @RequestParam(required = false,defaultValue = "0") Integer pageNo
                        ){
        return itemService.getItemsByCategory(categoryName,pageNo);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ItemDto>> findByPriceBetween(@RequestParam int minPrice, @RequestParam int highPrice){
        return itemService.findByPriceBetween(minPrice, highPrice);
    }

    @GetMapping("/instant-ready")
    public ResponseEntity<List<ItemDto>> getInstantReadyItems(){
        return itemService.getInstantReadyItems();
    }

}
