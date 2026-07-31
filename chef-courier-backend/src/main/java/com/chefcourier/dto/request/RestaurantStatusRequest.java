package com.chefcourier.dto.request;

import com.chefcourier.enums.RestaurantStatus;
import jakarta.validation.constraints.NotNull;

public record RestaurantStatusRequest(

        @NotNull
        RestaurantStatus status

) {
}
