package com.chefcourier.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MenuItemRequest(

        Long categoryId,

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal price,

        String imageUrl,

        boolean vegetarian,

        boolean available,

        @Min(1)
        int preparationMinutes

) {
}
