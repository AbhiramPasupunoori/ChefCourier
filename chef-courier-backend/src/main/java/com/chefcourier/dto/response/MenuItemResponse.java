package com.chefcourier.dto.response;

import java.math.BigDecimal;

public record MenuItemResponse(

        Long id,
        Long restaurantId,
        Long categoryId,
        String categoryName,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        boolean vegetarian,
        boolean available,
        int preparationMinutes

) {
}
