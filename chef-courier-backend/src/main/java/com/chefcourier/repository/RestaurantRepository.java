package com.chefcourier.repository;

import com.chefcourier.entity.Restaurant;
import com.chefcourier.enums.RestaurantStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface RestaurantRepository
        extends JpaRepository<Restaurant, Long> {

    List<Restaurant>
    findByStatusAndActiveTrueOrderByCreatedAtDesc(
            RestaurantStatus status
    );

    List<Restaurant>
    findByOwnerIdOrderByCreatedAtDesc(
            Long ownerId
    );

    Optional<Restaurant> findByIdAndOwnerId(
            Long restaurantId,
            Long ownerId
    );

    Optional<Restaurant> findByNameIgnoreCase(
            String name
    );

    List<Restaurant>
    findByStatusOrderByCreatedAtDesc(
            RestaurantStatus status
    );

    @Query("""
            SELECT restaurant
            FROM Restaurant restaurant
            WHERE restaurant.status =
                  com.chefcourier.enums.RestaurantStatus.APPROVED
              AND restaurant.active = true
              AND (
                    LOWER(restaurant.name)
                    LIKE LOWER(CONCAT('%', :query, '%'))
                    OR LOWER(restaurant.cuisineType)
                    LIKE LOWER(CONCAT('%', :query, '%'))
                    OR LOWER(restaurant.city)
                    LIKE LOWER(CONCAT('%', :query, '%'))
                  )
            ORDER BY restaurant.createdAt DESC
            """)
    List<Restaurant> search(
            @Param("query")
            String query
    );
}
