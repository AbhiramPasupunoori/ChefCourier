package com.chefcourier.controller;

import com.chefcourier.dto.request.*;
import com.chefcourier.dto.response.CartResponse;
import com.chefcourier.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    private final CartService cartService;

    public CartController(
            CartService cartService
    ) {
        this.cartService =
                cartService;
    }

    @GetMapping
    public CartResponse cart(
            @AuthenticationPrincipal
            Jwt jwt
    ) {
        return cartService.viewCart(
                jwt.getSubject()
        );
    }

    @PostMapping("/items")
    public CartResponse addItem(
            @AuthenticationPrincipal
            Jwt jwt,

            @Valid
            @RequestBody
            AddCartItemRequest request
    ) {
        return cartService.addItem(
                jwt.getSubject(),
                request
        );
    }

    @PutMapping("/items/{itemId}")
    public CartResponse updateItem(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long itemId,

            @Valid
            @RequestBody
            UpdateCartItemRequest request
    ) {
        return cartService.updateItem(
                jwt.getSubject(),
                itemId,
                request
        );
    }

    @DeleteMapping("/items/{itemId}")
    public CartResponse removeItem(
            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long itemId
    ) {
        return cartService.removeItem(
                jwt.getSubject(),
                itemId
        );
    }
}
