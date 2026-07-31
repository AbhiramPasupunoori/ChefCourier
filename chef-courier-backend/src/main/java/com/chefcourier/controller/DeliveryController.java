package com.chefcourier.controller;

import com.chefcourier.dto.request.DeliveryStatusRequest;
import com.chefcourier.dto.response.DeliveryResponse;
import com.chefcourier.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery/tasks")
public class DeliveryController {

    private final DeliveryService
            deliveryService;

    public DeliveryController(
            DeliveryService deliveryService
    ) {
        this.deliveryService =
                deliveryService;
    }

    @GetMapping
    public List<DeliveryResponse> tasks(
            @AuthenticationPrincipal
            Jwt jwt
    ) {
        return deliveryService.getTasks(
                jwt.getSubject()
        );
    }

    @PatchMapping("/{deliveryId}/status")
    public DeliveryResponse updateStatus(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long deliveryId,

            @Valid
            @RequestBody
            DeliveryStatusRequest request
    ) {
        return deliveryService.updateStatus(
                jwt.getSubject(),
                deliveryId,
                request.status()
        );
    }
}
