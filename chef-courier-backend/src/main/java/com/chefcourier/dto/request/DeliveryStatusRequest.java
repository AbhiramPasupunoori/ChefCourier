package com.chefcourier.dto.request;

import com.chefcourier.enums.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public record DeliveryStatusRequest(

        @NotNull
        DeliveryStatus status

) {
}
