package com.chefcourier.repository;

import com.chefcourier.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByIdAndCartId(
            Long itemId,
            Long cartId
    );

    Optional<CartItem>
    findByCartIdAndMenuItemId(
            Long cartId,
            Long menuItemId
    );
}
