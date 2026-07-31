package com.chefcourier.repository;

import com.chefcourier.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface MenuItemRepository
        extends JpaRepository<MenuItem, Long> {

    List<MenuItem>
    findByRestaurantIdOrderByNameAsc(
            Long restaurantId
    );

    List<MenuItem>
    findByRestaurantIdAndAvailableTrueOrderByNameAsc(
            Long restaurantId
    );

    Optional<MenuItem>
    findByIdAndRestaurantId(
            Long menuItemId,
            Long restaurantId
    );

    boolean existsByRestaurantIdAndNameIgnoreCase(
            Long restaurantId,
            String name
    );
}
