package com.chefcourier.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(

        Long id,
        Long restaurantId,
        String restaurantName,
        List<CartItemResponse> items,
        BigDecimal subtotal

) {
}
