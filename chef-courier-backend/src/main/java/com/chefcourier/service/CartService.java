package com.chefcourier.service;

import com.chefcourier.dto.request.*;
import com.chefcourier.dto.response.CartResponse;
import com.chefcourier.entity.*;
import com.chefcourier.enums.RestaurantStatus;
import com.chefcourier.exception.*;
import com.chefcourier.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserLookupService userLookupService;
    private final MapperService mapperService;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            MenuItemRepository menuItemRepository,
            UserLookupService userLookupService,
            MapperService mapperService
    ) {
        this.cartRepository =
                cartRepository;

        this.cartItemRepository =
                cartItemRepository;

        this.menuItemRepository =
                menuItemRepository;

        this.userLookupService =
                userLookupService;

        this.mapperService =
                mapperService;
    }

    @Transactional
    public Cart getCartEntity(
            String email
    ) {
        User customer =
                userLookupService.getByEmail(email);

        return cartRepository
                .findByCustomerId(
                        customer.getId()
                )
                .orElseGet(() -> {
                    Cart cart = new Cart();

                    cart.setCustomer(customer);

                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public CartResponse viewCart(
            String email
    ) {
        return mapperService.cart(
                getCartEntity(email)
        );
    }

    @Transactional
    public CartResponse addItem(
            String email,
            AddCartItemRequest request
    ) {
        Cart cart =
                getCartEntity(email);

        MenuItem menuItem =
                menuItemRepository
                        .findById(
                                request.menuItemId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Menu item was not found"
                                )
                        );

        if (!menuItem.isAvailable()) {
            throw new AppException(
                    "Menu item is unavailable"
            );
        }

        Restaurant restaurant =
                menuItem.getRestaurant();

        if (restaurant.getStatus()
                != RestaurantStatus.APPROVED
                || !restaurant.isActive()) {

            throw new AppException(
                    "Restaurant is unavailable"
            );
        }

        if (cart.getRestaurant() != null
                && !cart.getRestaurant()
                .getId()
                .equals(restaurant.getId())) {

            throw new AppException(
                    "A cart can contain items from only one restaurant"
            );
        }

        cart.setRestaurant(restaurant);

        CartItem item =
                cartItemRepository
                        .findByCartIdAndMenuItemId(
                                cart.getId(),
                                menuItem.getId()
                        )
                        .orElseGet(() -> {
                            CartItem newItem =
                                    new CartItem();

                            newItem.setCart(cart);
                            newItem.setMenuItem(menuItem);
                            newItem.setQuantity(0);

                            cart.getItems().add(
                                    newItem
                            );

                            return newItem;
                        });

        item.setQuantity(
                item.getQuantity()
                        + request.quantity()
        );

        item.setUnitPrice(
                menuItem.getPrice()
        );

        cartItemRepository.save(item);
        cartRepository.save(cart);

        return mapperService.cart(cart);
    }

    @Transactional
    public CartResponse updateItem(
            String email,
            Long itemId,
            UpdateCartItemRequest request
    ) {
        Cart cart =
                getCartEntity(email);

        CartItem item =
                cartItemRepository
                        .findByIdAndCartId(
                                itemId,
                                cart.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item was not found"
                                )
                        );

        item.setQuantity(
                request.quantity()
        );

        cartItemRepository.save(item);

        return mapperService.cart(cart);
    }

    @Transactional
    public CartResponse removeItem(
            String email,
            Long itemId
    ) {
        Cart cart =
                getCartEntity(email);

        CartItem item =
                cartItemRepository
                        .findByIdAndCartId(
                                itemId,
                                cart.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item was not found"
                                )
                        );

        cart.getItems().removeIf(
                existing ->
                        existing.getId()
                                .equals(itemId)
        );

        cartItemRepository.delete(item);

        if (cart.getItems().isEmpty()) {
            cart.setRestaurant(null);
        }

        cartRepository.save(cart);

        return mapperService.cart(cart);
    }

    @Transactional
    public void clearCart(
            Cart cart
    ) {
        cartItemRepository.deleteAll(
                cart.getItems()
        );

        cart.getItems().clear();
        cart.setRestaurant(null);

        cartRepository.save(cart);
    }
}
