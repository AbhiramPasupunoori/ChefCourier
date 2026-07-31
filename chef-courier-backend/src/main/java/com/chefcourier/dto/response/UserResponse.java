package com.chefcourier.dto.response;

import com.chefcourier.enums.Role;

public record UserResponse(

        Long id,
        String fullName,
        String email,
        String phoneNumber,
        Role role,
        boolean enabled

) {
}
