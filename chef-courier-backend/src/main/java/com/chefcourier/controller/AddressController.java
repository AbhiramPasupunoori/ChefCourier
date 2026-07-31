package com.chefcourier.controller;

import com.chefcourier.dto.request.AddressRequest;
import com.chefcourier.dto.response.AddressResponse;
import com.chefcourier.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(
            AddressService addressService
    ) {
        this.addressService =
                addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponse>
    createAddress(

            @AuthenticationPrincipal
            Jwt jwt,

            @Valid
            @RequestBody
            AddressRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        addressService.createAddress(
                                jwt.getSubject(),
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>>
    getAddresses(

            @AuthenticationPrincipal
            Jwt jwt
    ) {
        return ResponseEntity.ok(
                addressService.getAddresses(
                        jwt.getSubject()
                )
        );
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse>
    updateAddress(

            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long addressId,

            @Valid
            @RequestBody
            AddressRequest request
    ) {
        return ResponseEntity.ok(
                addressService.updateAddress(
                        jwt.getSubject(),
                        addressId,
                        request
                )
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(

            @AuthenticationPrincipal
            Jwt jwt,

            @PathVariable
            Long addressId
    ) {
        addressService.deleteAddress(
                jwt.getSubject(),
                addressId
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}
