package com.chefcourier.controller;

import com.chefcourier.dto.response.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<HealthResponse> checkHealth() {
        HealthResponse response = new HealthResponse(
                "ChefCourier",
                "ChefCourier backend is running successfully",
                "UP",
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
