package com.chefcourier.dto.request;

import com.chefcourier.enums.Role;
import jakarta.validation.constraints.*;

public record RegisterRequest(

        @NotBlank
        @Size(min = 2, max = 100)
        String fullName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 72)
        String password,

        @NotBlank
        String phoneNumber,

        @NotNull
        Role role

) {
}
