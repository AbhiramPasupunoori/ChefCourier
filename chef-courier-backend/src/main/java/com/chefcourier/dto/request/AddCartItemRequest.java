package com.chefcourier.dto.request;

import jakarta.validation.constraints.*;

public record AddCartItemRequest(

        @NotNull
        Long menuItemId,

        @Min(1)
        int quantity

) {
}
