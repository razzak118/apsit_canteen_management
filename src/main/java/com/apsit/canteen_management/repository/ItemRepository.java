package com.apsit.canteen_management.repository;

import com.apsit.canteen_management.entity.Item;
import com.apsit.canteen_management.enums.ItemCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    Optional<Item> findByItemNameIgnoreCase(String name);

    Optional<List<Item>> findAllByCategory(ItemCategory category);

    Optional<List<Item>> findByPriceBetween(int minPrice, int highPrice);
}
