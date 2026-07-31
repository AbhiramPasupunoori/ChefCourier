package com.chefcourier.controller;

import com.chefcourier.dto.response.*;
import com.chefcourier.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService
            restaurantService;

    private final MenuService
            menuService;

    public RestaurantController(
            RestaurantService restaurantService,
            MenuService menuService
    ) {
        this.restaurantService =
                restaurantService;

        this.menuService =
                menuService;
    }

    @GetMapping
    public List<RestaurantResponse> restaurants(
            @RequestParam(required = false)
            String search
    ) {
        return restaurantService
                .getPublicRestaurants(search);
    }

    @GetMapping("/{restaurantId}")
    public RestaurantResponse restaurant(
            @PathVariable
            Long restaurantId
    ) {
        return restaurantService
                .getPublicRestaurant(
                        restaurantId
                );
    }

    @GetMapping("/{restaurantId}/categories")
    public List<MenuCategoryResponse> categories(
            @PathVariable
            Long restaurantId
    ) {
        return menuService.categories(
                restaurantId
        );
    }

    @GetMapping("/{restaurantId}/menu-items")
    public List<MenuItemResponse> menuItems(
            @PathVariable
            Long restaurantId
    ) {
        return menuService.publicItems(
                restaurantId
        );
    }
}
