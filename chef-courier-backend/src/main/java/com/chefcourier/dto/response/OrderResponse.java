package com.chefcourier.dto.response;

import com.chefcourier.enums.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(

        Long id,
        Long restaurantId,
        String restaurantName,
        Long customerId,
        String customerName,
        String deliveryAddress,
        OrderStatus status,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        BigDecimal subtotal,
        BigDecimal deliveryFee,
        BigDecimal totalAmount,
        String specialInstructions,
        List<OrderItemResponse> items,
        LocalDateTime createdAt

) {
}
