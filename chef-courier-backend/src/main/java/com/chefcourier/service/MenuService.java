package com.chefcourier.service;

import com.chefcourier.dto.request.*;
import com.chefcourier.dto.response.*;
import com.chefcourier.entity.*;
import com.chefcourier.exception.ResourceNotFoundException;
import com.chefcourier.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuCategoryRepository
            categoryRepository;

    private final MenuItemRepository
            menuItemRepository;

    private final RestaurantService
            restaurantService;

    private final MapperService
            mapperService;

    public MenuService(
            MenuCategoryRepository categoryRepository,
            MenuItemRepository menuItemRepository,
            RestaurantService restaurantService,
            MapperService mapperService
    ) {
        this.categoryRepository =
                categoryRepository;

        this.menuItemRepository =
                menuItemRepository;

        this.restaurantService =
                restaurantService;

        this.mapperService =
                mapperService;
    }

    public List<MenuCategoryResponse> categories(
            Long restaurantId
    ) {
        return categoryRepository
                .findByRestaurantIdOrderByNameAsc(
                        restaurantId
                )
                .stream()
                .map(mapperService::category)
                .toList();
    }

    public List<MenuItemResponse> publicItems(
            Long restaurantId
    ) {
        return menuItemRepository
                .findByRestaurantIdAndAvailableTrueOrderByNameAsc(
                        restaurantId
                )
                .stream()
                .map(mapperService::menuItem)
                .toList();
    }

    public List<MenuItemResponse> ownerItems(
            String email,
            Long restaurantId
    ) {
        restaurantService.getOwnedRestaurant(
                email,
                restaurantId
        );

        return menuItemRepository
                .findByRestaurantIdOrderByNameAsc(
                        restaurantId
                )
                .stream()
                .map(mapperService::menuItem)
                .toList();
    }

    @Transactional
    public MenuCategoryResponse createCategory(
            String email,
            Long restaurantId,
            MenuCategoryRequest request
    ) {
        Restaurant restaurant =
                restaurantService
                        .getOwnedRestaurant(
                                email,
                                restaurantId
                        );

        MenuCategory category =
                new MenuCategory();

        category.setRestaurant(restaurant);

        category.setName(
                request.name().trim()
        );

        return mapperService.category(
                categoryRepository.save(
                        category
                )
        );
    }

    @Transactional
    public MenuItemResponse createMenuItem(
            String email,
            Long restaurantId,
            MenuItemRequest request
    ) {
        Restaurant restaurant =
                restaurantService
                        .getOwnedRestaurant(
                                email,
                                restaurantId
                        );

        MenuItem item = new MenuItem();

        item.setRestaurant(restaurant);

        copyRequest(
                restaurantId,
                request,
                item
        );

        return mapperService.menuItem(
                menuItemRepository.save(item)
        );
    }

    @Transactional
    public MenuItemResponse updateMenuItem(
            String email,
            Long restaurantId,
            Long menuItemId,
            MenuItemRequest request
    ) {
        restaurantService.getOwnedRestaurant(
                email,
                restaurantId
        );

        MenuItem item =
                menuItemRepository
                        .findByIdAndRestaurantId(
                                menuItemId,
                                restaurantId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Menu item was not found"
                                )
                        );

        copyRequest(
                restaurantId,
                request,
                item
        );

        return mapperService.menuItem(
                menuItemRepository.save(item)
        );
    }

    @Transactional
    public void deleteMenuItem(
            String email,
            Long restaurantId,
            Long menuItemId
    ) {
        restaurantService.getOwnedRestaurant(
                email,
                restaurantId
        );

        MenuItem item =
                menuItemRepository
                        .findByIdAndRestaurantId(
                                menuItemId,
                                restaurantId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Menu item was not found"
                                )
                        );

        menuItemRepository.delete(item);
    }

    private void copyRequest(
            Long restaurantId,
            MenuItemRequest request,
            MenuItem item
    ) {
        if (request.categoryId() == null) {
            item.setCategory(null);
        } else {
            MenuCategory category =
                    categoryRepository
                            .findByIdAndRestaurantId(
                                    request.categoryId(),
                                    restaurantId
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Menu category was not found"
                                    )
                            );

            item.setCategory(category);
        }

        item.setName(
                request.name().trim()
        );

        item.setDescription(
                request.description().trim()
        );

        item.setPrice(request.price());
        item.setImageUrl(request.imageUrl());
        item.setVegetarian(request.vegetarian());
        item.setAvailable(request.available());

        item.setPreparationMinutes(
                request.preparationMinutes()
        );
    }
}
