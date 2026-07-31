package com.chefcourier.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MenuCategoryRequest(

        @NotBlank
        String name

) {
}
