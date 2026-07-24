package com.chefcourier.dto.response;

import java.time.LocalDateTime;

public record HealthResponse(
        String application,
        String message,
        String status,
        LocalDateTime timestamp
) {
}
