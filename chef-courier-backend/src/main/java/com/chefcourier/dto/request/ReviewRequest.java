package com.chefcourier.dto.request;

import jakarta.validation.constraints.*;

public record ReviewRequest(

        @Min(1)
        @Max(5)
        int rating,

        @NotBlank
        @Size(max = 500)
        String comment

) {
}
