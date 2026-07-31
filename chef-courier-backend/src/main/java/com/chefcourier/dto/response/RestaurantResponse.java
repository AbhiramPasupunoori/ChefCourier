package com.chefcourier.dto.response;

import com.chefcourier.enums.RestaurantStatus;
import java.time.LocalTime;

public record RestaurantResponse(

        Long id,
        Long ownerId,
        String ownerName,
        String name,
        String description,
        String cuisineType,
        String phone,
        String address,
        String city,
        String imageUrl,
        LocalTime openingTime,
        LocalTime closingTime,
        RestaurantStatus status,
        boolean active,
        double averageRating

) {
}
