package com.chefcourier.repository;

import com.chefcourier.entity.Cart;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface CartRepository
        extends JpaRepository<Cart, Long> {

    @EntityGraph(
            attributePaths = {
                    "items",
                    "items.menuItem",
                    "restaurant"
            }
    )
    Optional<Cart> findByCustomerId(
            Long customerId
    );
}
