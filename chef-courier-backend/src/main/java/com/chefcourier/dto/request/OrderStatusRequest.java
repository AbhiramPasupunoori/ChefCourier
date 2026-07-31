package com.chefcourier.dto.request;

import com.chefcourier.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(

        @NotNull
        OrderStatus status

) {
}
