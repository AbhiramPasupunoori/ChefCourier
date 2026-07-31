package com.chefcourier.repository;

import com.chefcourier.entity.Review;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    boolean existsByOrderId(
            Long orderId
    );

    List<Review>
    findByRestaurantIdOrderByCreatedAtDesc(
            Long restaurantId
    );

    @Query("""
            SELECT AVG(review.rating)
            FROM Review review
            WHERE review.restaurant.id =
                  :restaurantId
            """)
    Double averageForRestaurant(
            @Param("restaurantId")
            Long restaurantId
    );
}
