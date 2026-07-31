package com.chefcourier.dto.response;

public record AddressResponse(

        Long id,
        String label,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String postalCode,
        String country,
        boolean defaultAddress

) {
}
