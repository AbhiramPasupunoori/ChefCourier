package com.chefcourier.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalTime;

public record RestaurantRequest(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String cuisineType,

        @NotBlank
        String phone,

        @NotBlank
        String address,

        @NotBlank
        String city,

        String imageUrl,

        @NotNull
        LocalTime openingTime,

        @NotNull
        LocalTime closingTime

) {
}
