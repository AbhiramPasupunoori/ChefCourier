package com.chefcourier.service;

import com.chefcourier.dto.response.*;
import com.chefcourier.entity.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MapperService {

    public UserResponse user(
            User user
    ) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isEnabled()
        );
    }

    public AddressResponse address(
            Address address
    ) {
        return new AddressResponse(
                address.getId(),
                address.getLabel(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry(),
                address.isDefaultAddress()
        );
    }

public RestaurantResponse restaurant(
        Restaurant restaurant
) {
    return new RestaurantResponse(
            restaurant.getId(),
            restaurant.getOwner().getId(),
            restaurant.getOwner().getFullName(),
            restaurant.getName(),
            restaurant.getDescription(),
            restaurant.getCuisineType(),
            restaurant.getPhone(),
            restaurant.getAddress(),
            restaurant.getCity(),
            restaurant.getImageUrl(),
            restaurant.getOpeningTime(),
            restaurant.getClosingTime(),
            restaurant.getStatus(),
            restaurant.isActive(),
            restaurant.getAverageRating()
    );
}

public MenuCategoryResponse category(
        MenuCategory category
) {
    return new MenuCategoryResponse(
            category.getId(),
            category.getName()
    );
}

public MenuItemResponse menuItem(
        MenuItem menuItem
) {
    return new MenuItemResponse(
            menuItem.getId(),
            menuItem.getRestaurant().getId(),
            menuItem.getCategory() == null
                    ? null
                    : menuItem.getCategory().getId(),
            menuItem.getCategory() == null
                    ? null
                    : menuItem.getCategory().getName(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getImageUrl(),
            menuItem.isVegetarian(),
            menuItem.isAvailable(),
            menuItem.getPreparationMinutes()
    );
}

public CartResponse cart(
        Cart cart
) {
    List<CartItemResponse> itemResponses =
            cart.getItems()
                    .stream()
                    .map(item -> {
                        BigDecimal total =
                                item.getUnitPrice()
                                        .multiply(
                                                BigDecimal.valueOf(
                                                        item.getQuantity()
                                                )
                                        );

                        return new CartItemResponse(
                                item.getId(),
                                item.getMenuItem().getId(),
                                item.getMenuItem().getName(),
                                item.getMenuItem().getImageUrl(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                total
                        );
                    })
                    .toList();

    BigDecimal subtotal =
            itemResponses.stream()
                    .map(
                            CartItemResponse::totalPrice
                    )
                    .reduce(
                            BigDecimal.ZERO,
                            BigDecimal::add
                    );

    return new CartResponse(
            cart.getId(),
            cart.getRestaurant() == null
                    ? null
                    : cart.getRestaurant().getId(),
            cart.getRestaurant() == null
                    ? null
                    : cart.getRestaurant().getName(),
            itemResponses,
            subtotal
    );
}

public OrderResponse order(
        CustomerOrder order
) {
    List<OrderItemResponse> itemResponses =
            order.getItems()
                    .stream()
                    .map(item ->
                            new OrderItemResponse(
                                    item.getId(),
                                    item.getMenuItemId(),
                                    item.getItemName(),
                                    item.getQuantity(),
                                    item.getUnitPrice(),
                                    item.getTotalPrice()
                            )
                    )
                    .toList();

    return new OrderResponse(
            order.getId(),
            order.getRestaurant().getId(),
            order.getRestaurant().getName(),
            order.getCustomer().getId(),
            order.getCustomer().getFullName(),
            order.getDeliveryAddress(),
            order.getStatus(),
            order.getPaymentMethod(),
            order.getPaymentStatus(),
            order.getSubtotal(),
            order.getDeliveryFee(),
            order.getTotalAmount(),
            order.getSpecialInstructions(),
            itemResponses,
            order.getCreatedAt()
    );
}

public DeliveryResponse delivery(
        Delivery delivery
) {
    CustomerOrder order =
            delivery.getOrder();

    return new DeliveryResponse(
            delivery.getId(),
            order.getId(),
            order.getRestaurant().getName(),
            order.getRestaurant().getAddress(),
            order.getCustomer().getFullName(),
            order.getDeliveryAddress(),
            delivery.getStatus(),
            delivery.getAssignedAt(),
            delivery.getPickedUpAt(),
            delivery.getDeliveredAt()
    );
}

public ReviewResponse review(
        Review review
) {
    return new ReviewResponse(
            review.getId(),
            review.getRestaurant().getId(),
            review.getOrder().getId(),
            review.getCustomer().getFullName(),
            review.getRating(),
            review.getComment(),
            review.getCreatedAt()
    );
}
}
