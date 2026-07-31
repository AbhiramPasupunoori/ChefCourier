package com.chefcourier.controller;

import com.chefcourier.dto.request.*;
import com.chefcourier.dto.response.*;
import com.chefcourier.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
public class CustomerOrderController {

    private final OrderService orderService;
    private final ReviewService reviewService;

    public CustomerOrderController(
            OrderService orderService,
            ReviewService reviewService
    ) {
        this.orderService =
                orderService;

        this.reviewService =
                reviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(
            @AuthenticationPrincipal
            Jwt jwt,

            @Valid
            @RequestBody
            PlaceOrderRequest request
    ) {
        return orderService.placeOrder(
                jwt.getSubject(),
                request
        );
    }

    @GetMapping
    public List<OrderResponse> orders(
            @AuthenticationPrincipal
            Jwt jwt
    ) {
        return orderService.customerOrders(
                jwt.getSubject()
        );
    }

    @PostMapping("/{orderId}/pay")
    public OrderResponse pay(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long orderId
    ) {
        return orderService.payOrder(
                jwt.getSubject(),
                orderId
        );
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderResponse cancel(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long orderId
    ) {
        return orderService.cancelOrder(
                jwt.getSubject(),
                orderId
        );
    }

    @PostMapping("/{orderId}/review")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse review(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long orderId,

            @Valid
            @RequestBody
            ReviewRequest request
    ) {
        return reviewService.createReview(
                jwt.getSubject(),
                orderId,
                request
        );
    }
}
