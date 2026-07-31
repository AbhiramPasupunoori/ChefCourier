package com.chefcourier.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse(

        Long id,
        Long menuItemId,
        String itemName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice

) {
}
