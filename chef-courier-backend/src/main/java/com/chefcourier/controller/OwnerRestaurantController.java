package com.chefcourier.controller;

import com.chefcourier.dto.request.*;
import com.chefcourier.dto.response.*;
import com.chefcourier.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/restaurants")
public class OwnerRestaurantController {

    private final RestaurantService
            restaurantService;

    private final MenuService
            menuService;

    private final OrderService
            orderService;

    public OwnerRestaurantController(
            RestaurantService restaurantService,
            MenuService menuService,
            OrderService orderService
    ) {
        this.restaurantService =
                restaurantService;

        this.menuService =
                menuService;

        this.orderService =
                orderService;
    }

    @GetMapping
    public List<RestaurantResponse> restaurants(
            @AuthenticationPrincipal
            Jwt jwt
    ) {
        return restaurantService
                .getOwnerRestaurants(
                        jwt.getSubject()
                );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse create(
            @AuthenticationPrincipal
            Jwt jwt,

            @Valid
            @RequestBody
            RestaurantRequest request
    ) {
        return restaurantService
                .createRestaurant(
                        jwt.getSubject(),
                        request
                );
    }

    @PutMapping("/{restaurantId}")
    public RestaurantResponse update(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long restaurantId,

            @Valid
            @RequestBody
            RestaurantRequest request
    ) {
        return restaurantService
                .updateRestaurant(
                        jwt.getSubject(),
                        restaurantId,
                        request
                );
    }

    @PostMapping("/{restaurantId}/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuCategoryResponse createCategory(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long restaurantId,

            @Valid
            @RequestBody
            MenuCategoryRequest request
    ) {
        return menuService
                .createCategory(
                        jwt.getSubject(),
                        restaurantId,
                        request
                );
    }

    @GetMapping("/{restaurantId}/menu-items")
    public List<MenuItemResponse> menuItems(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long restaurantId
    ) {
        return menuService.ownerItems(
                jwt.getSubject(),
                restaurantId
        );
    }

    @PostMapping("/{restaurantId}/menu-items")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse createMenuItem(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long restaurantId,

            @Valid
            @RequestBody
            MenuItemRequest request
    ) {
        return menuService
                .createMenuItem(
                        jwt.getSubject(),
                        restaurantId,
                        request
                );
    }

    @PutMapping(
            "/{restaurantId}/menu-items/{menuItemId}"
    )
    public MenuItemResponse updateMenuItem(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long restaurantId,

            @PathVariable
            Long menuItemId,

            @Valid
            @RequestBody
            MenuItemRequest request
    ) {
        return menuService
                .updateMenuItem(
                        jwt.getSubject(),
                        restaurantId,
                        menuItemId,
                        request
                );
    }

    @DeleteMapping(
            "/{restaurantId}/menu-items/{menuItemId}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuItem(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long restaurantId,

            @PathVariable
            Long menuItemId
    ) {
        menuService.deleteMenuItem(
                jwt.getSubject(),
                restaurantId,
                menuItemId
        );
    }

    @GetMapping("/{restaurantId}/orders")
    public List<OrderResponse> orders(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long restaurantId
    ) {
        return orderService.ownerOrders(
                jwt.getSubject(),
                restaurantId
        );
    }

    @PatchMapping(
            "/{restaurantId}/orders/{orderId}/status"
    )
    public OrderResponse updateOrderStatus(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long restaurantId,

            @PathVariable
            Long orderId,

            @Valid
            @RequestBody
            OrderStatusRequest request
    ) {
        return orderService.ownerUpdateStatus(
                jwt.getSubject(),
                restaurantId,
                orderId,
                request.status()
        );
    }
}
