package com.chefcourier.dto.response;

import java.math.BigDecimal;

public record CartItemResponse(

        Long id,
        Long menuItemId,
        String name,
        String imageUrl,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice

) {
}
