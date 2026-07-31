package com.chefcourier.dto.response;

import com.chefcourier.enums.DeliveryStatus;
import java.time.LocalDateTime;

public record DeliveryResponse(

        Long id,
        Long orderId,
        String restaurantName,
        String restaurantAddress,
        String customerName,
        String deliveryAddress,
        DeliveryStatus status,
        LocalDateTime assignedAt,
        LocalDateTime pickedUpAt,
        LocalDateTime deliveredAt

) {
}
