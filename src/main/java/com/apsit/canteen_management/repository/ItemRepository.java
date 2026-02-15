package com.apsit.canteen_management.repository;

import com.apsit.canteen_management.entity.MenuItem;
import com.apsit.canteen_management.enums.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<MenuItem,Long> {
    Optional<MenuItem> findByItemNameIgnoreCase(String name);

    Optional<List<MenuItem>> findAllByCategory(ItemCategory category);

    Optional<List<MenuItem>> findByPriceBetween(int minPrice, int highPrice);
}
