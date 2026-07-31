package com.chefcourier.dto.request;

import jakarta.validation.constraints.NotNull;

public record AssignDeliveryRequest(

        @NotNull
        Long deliveryPartnerId

) {
}
