package com.chefcourier.dto.response;

import java.time.LocalDateTime;

public record ReviewResponse(

        Long id,
        Long restaurantId,
        Long orderId,
        String customerName,
        int rating,
        String comment,
        LocalDateTime createdAt

) {
}
