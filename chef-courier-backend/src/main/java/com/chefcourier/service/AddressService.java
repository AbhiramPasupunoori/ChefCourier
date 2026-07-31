package com.chefcourier.service;

import com.chefcourier.dto.request.AddressRequest;
import com.chefcourier.dto.response.AddressResponse;
import com.chefcourier.entity.Address;
import com.chefcourier.entity.User;
import com.chefcourier.exception.ResourceNotFoundException;
import com.chefcourier.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserLookupService userLookupService;
    private final MapperService mapperService;

    public AddressService(
            AddressRepository addressRepository,
            UserLookupService userLookupService,
            MapperService mapperService
    ) {
        this.addressRepository =
                addressRepository;

        this.userLookupService =
                userLookupService;

        this.mapperService =
                mapperService;
    }

    @Transactional
    public AddressResponse createAddress(
            String email,
            AddressRequest request
    ) {
        User user =
                userLookupService.getByEmail(email);

        List<Address> existingAddresses =
                addressRepository
                        .findAllByUserIdOrderByDefaultAddressDescCreatedAtDesc(
                                user.getId()
                        );

        boolean shouldBeDefault =
                request.defaultAddress()
                        || existingAddresses.isEmpty();

        if (shouldBeDefault) {
            existingAddresses.forEach(address ->
                    address.setDefaultAddress(false)
            );

            addressRepository.saveAll(
                    existingAddresses
            );
        }

        Address address = new Address();

        address.setUser(user);

        copyRequestToAddress(
                request,
                address
        );

        address.setDefaultAddress(
                shouldBeDefault
        );

        return mapperService.address(
                addressRepository.save(address)
        );
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(
            String email
    ) {
        User user =
                userLookupService.getByEmail(email);

        return addressRepository
                .findAllByUserIdOrderByDefaultAddressDescCreatedAtDesc(
                        user.getId()
                )
                .stream()
                .map(mapperService::address)
                .toList();
    }

    @Transactional
    public AddressResponse updateAddress(
            String email,
            Long addressId,
            AddressRequest request
    ) {
        User user =
                userLookupService.getByEmail(email);

        Address address =
                addressRepository
                        .findByIdAndUserId(
                                addressId,
                                user.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address was not found"
                                )
                        );

        if (request.defaultAddress()) {
            List<Address> addresses =
                    addressRepository
                            .findAllByUserIdOrderByDefaultAddressDescCreatedAtDesc(
                                    user.getId()
                            );

            addresses.stream()
                    .filter(existing ->
                            !existing.getId()
                                    .equals(addressId)
                    )
                    .forEach(existing ->
                            existing.setDefaultAddress(
                                    false
                            )
                    );

            addressRepository.saveAll(
                    addresses
            );
        }

        copyRequestToAddress(
                request,
                address
        );

        address.setDefaultAddress(
                request.defaultAddress()
        );

        return mapperService.address(
                addressRepository.save(address)
        );
    }

    @Transactional
    public void deleteAddress(
            String email,
            Long addressId
    ) {
        User user =
                userLookupService.getByEmail(email);

        Address address =
                addressRepository
                        .findByIdAndUserId(
                                addressId,
                                user.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address was not found"
                                )
                        );

        boolean wasDefault =
                address.isDefaultAddress();

        addressRepository.delete(address);
        addressRepository.flush();

        if (wasDefault) {
            List<Address> remainingAddresses =
                    addressRepository
                            .findAllByUserIdOrderByDefaultAddressDescCreatedAtDesc(
                                    user.getId()
                            );

            if (!remainingAddresses.isEmpty()) {
                Address newDefault =
                        remainingAddresses.getFirst();

                newDefault.setDefaultAddress(true);

                addressRepository.save(newDefault);
            }
        }
    }

    private void copyRequestToAddress(
            AddressRequest request,
            Address address
    ) {
        address.setLabel(
                request.label().trim()
        );

        address.setAddressLine1(
                request.addressLine1().trim()
        );

        address.setAddressLine2(
                normalizeOptional(
                        request.addressLine2()
                )
        );

        address.setCity(
                request.city().trim()
        );

        address.setState(
                request.state().trim()
        );

        address.setPostalCode(
                request.postalCode().trim()
        );

        address.setCountry(
                request.country().trim()
        );
    }

    private String normalizeOptional(
            String value
    ) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
