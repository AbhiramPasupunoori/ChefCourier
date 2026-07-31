package com.chefcourier.repository;

import com.chefcourier.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface MenuCategoryRepository
        extends JpaRepository<MenuCategory, Long> {

    List<MenuCategory>
    findByRestaurantIdOrderByNameAsc(
            Long restaurantId
    );

    Optional<MenuCategory>
    findByIdAndRestaurantId(
            Long categoryId,
            Long restaurantId
    );
}
