package com.chefcourier.controller;

import com.chefcourier.dto.request.*;
import com.chefcourier.dto.response.*;
import com.chefcourier.enums.*;
import com.chefcourier.repository.UserRepository;
import com.chefcourier.service.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final RestaurantService
            restaurantService;

    private final OrderService
            orderService;

    private final DeliveryService
            deliveryService;

    private final UserRepository
            userRepository;

    private final MapperService
            mapperService;

    public AdminController(
            RestaurantService restaurantService,
            OrderService orderService,
            DeliveryService deliveryService,
            UserRepository userRepository,
            MapperService mapperService
    ) {
        this.restaurantService =
                restaurantService;

        this.orderService =
                orderService;

        this.deliveryService =
                deliveryService;

        this.userRepository =
                userRepository;

        this.mapperService =
                mapperService;
    }

    @GetMapping("/restaurants")
    public List<RestaurantResponse> restaurants(
            @RequestParam(required = false)
            RestaurantStatus status
    ) {
        return restaurantService
                .getAdminRestaurants(status);
    }

    @PatchMapping(
            "/restaurants/{restaurantId}/status"
    )
    public RestaurantResponse restaurantStatus(
            @PathVariable
            Long restaurantId,

            @Valid
            @RequestBody
            RestaurantStatusRequest request
    ) {
        return restaurantService.updateStatus(
                restaurantId,
                request.status()
        );
    }

    @GetMapping("/orders")
    public List<OrderResponse> orders(
            @RequestParam(required = false)
            OrderStatus status
    ) {
        return orderService.adminOrders(
                status
        );
    }

    @GetMapping("/delivery-partners")
    public List<UserResponse>
    deliveryPartners() {

        return userRepository
                .findByRoleAndEnabledTrue(
                        Role.DELIVERY_PARTNER
                )
                .stream()
                .map(mapperService::user)
                .toList();
    }

    @PostMapping(
            "/orders/{orderId}/assign-delivery"
    )
    public DeliveryResponse assignDelivery(
            @PathVariable
            Long orderId,

            @Valid
            @RequestBody
            AssignDeliveryRequest request
    ) {
        return deliveryService.assignPartner(
                orderId,
                request.deliveryPartnerId()
        );
    }
}
