package com.chefcourier.service;

import com.chefcourier.dto.request.RestaurantRequest;
import com.chefcourier.dto.response.RestaurantResponse;
import com.chefcourier.entity.*;
import com.chefcourier.enums.RestaurantStatus;
import com.chefcourier.exception.ResourceNotFoundException;
import com.chefcourier.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository
            restaurantRepository;

    private final UserLookupService
            userLookupService;

    private final MapperService
            mapperService;

    public RestaurantService(
            RestaurantRepository restaurantRepository,
            UserLookupService userLookupService,
            MapperService mapperService
    ) {
        this.restaurantRepository =
                restaurantRepository;

        this.userLookupService =
                userLookupService;

        this.mapperService =
                mapperService;
    }

    public List<RestaurantResponse>
    getPublicRestaurants(
            String search
    ) {
        List<Restaurant> restaurants;

        if (search == null || search.isBlank()) {
            restaurants =
                    restaurantRepository
                            .findByStatusAndActiveTrueOrderByCreatedAtDesc(
                                    RestaurantStatus.APPROVED
                            );
        } else {
            restaurants =
                    restaurantRepository.search(
                            search.trim()
                    );
        }

        return restaurants.stream()
                .map(mapperService::restaurant)
                .toList();
    }

    public RestaurantResponse getPublicRestaurant(
            Long restaurantId
    ) {
        Restaurant restaurant =
                restaurantRepository
                        .findById(restaurantId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Restaurant was not found"
                                )
                        );

        if (restaurant.getStatus()
                != RestaurantStatus.APPROVED
                || !restaurant.isActive()) {

            throw new ResourceNotFoundException(
                    "Restaurant is unavailable"
            );
        }

        return mapperService.restaurant(
                restaurant
        );
    }

    public List<RestaurantResponse>
    getOwnerRestaurants(
            String email
    ) {
        User owner =
                userLookupService.getByEmail(
                        email
                );

        return restaurantRepository
                .findByOwnerIdOrderByCreatedAtDesc(
                        owner.getId()
                )
                .stream()
                .map(mapperService::restaurant)
                .toList();
    }

    @Transactional
    public RestaurantResponse createRestaurant(
            String email,
            RestaurantRequest request
    ) {
        User owner =
                userLookupService.getByEmail(
                        email
                );

        Restaurant restaurant =
                new Restaurant();

        restaurant.setOwner(owner);

        copyRequest(
                request,
                restaurant
        );

        restaurant.setStatus(
                RestaurantStatus.PENDING
        );

        restaurant.setActive(false);

        return mapperService.restaurant(
                restaurantRepository.save(
                        restaurant
                )
        );
    }

    @Transactional
    public RestaurantResponse updateRestaurant(
            String email,
            Long restaurantId,
            RestaurantRequest request
    ) {
        Restaurant restaurant =
                getOwnedRestaurant(
                        email,
                        restaurantId
                );

        copyRequest(
                request,
                restaurant
        );

        restaurant.setStatus(
                RestaurantStatus.PENDING
        );

        restaurant.setActive(false);

        return mapperService.restaurant(
                restaurantRepository.save(
                        restaurant
                )
        );
    }

    public Restaurant getOwnedRestaurant(
            String email,
            Long restaurantId
    ) {
        User owner =
                userLookupService.getByEmail(
                        email
                );

        return restaurantRepository
                .findByIdAndOwnerId(
                        restaurantId,
                        owner.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Restaurant was not found"
                        )
                );
    }

    public List<RestaurantResponse>
    getAdminRestaurants(
            RestaurantStatus status
    ) {
        List<Restaurant> restaurants =
                status == null
                        ? restaurantRepository.findAll()
                        : restaurantRepository
                        .findByStatusOrderByCreatedAtDesc(
                                status
                        );

        return restaurants.stream()
                .map(mapperService::restaurant)
                .toList();
    }

    @Transactional
    public RestaurantResponse updateStatus(
            Long restaurantId,
            RestaurantStatus status
    ) {
        Restaurant restaurant =
                restaurantRepository
                        .findById(restaurantId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Restaurant was not found"
                                )
                        );

        restaurant.setStatus(status);

        restaurant.setActive(
                status == RestaurantStatus.APPROVED
        );

        return mapperService.restaurant(
                restaurantRepository.save(
                        restaurant
                )
        );
    }

    private void copyRequest(
            RestaurantRequest request,
            Restaurant restaurant
    ) {
        restaurant.setName(
                request.name().trim()
        );

        restaurant.setDescription(
                request.description().trim()
        );

        restaurant.setCuisineType(
                request.cuisineType().trim()
        );

        restaurant.setPhone(
                request.phone().trim()
        );

        restaurant.setAddress(
                request.address().trim()
        );

        restaurant.setCity(
                request.city().trim()
        );

        restaurant.setImageUrl(
                request.imageUrl()
        );

        restaurant.setOpeningTime(
                request.openingTime()
        );

        restaurant.setClosingTime(
                request.closingTime()
        );
    }
}
