package com.chefcourier.dto.request;

import com.chefcourier.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PlaceOrderRequest(

        @NotNull
        Long addressId,

        @NotNull
        PaymentMethod paymentMethod,

        String specialInstructions

) {
}
