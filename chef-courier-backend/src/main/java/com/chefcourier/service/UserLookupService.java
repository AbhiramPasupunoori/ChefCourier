package com.chefcourier.service;

import com.chefcourier.entity.User;
import com.chefcourier.exception.ResourceNotFoundException;
import com.chefcourier.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserLookupService {

    private final UserRepository userRepository;

    public UserLookupService(
            UserRepository userRepository
    ) {
        this.userRepository =
                userRepository;
    }

    public User getByEmail(
            String email
    ) {
        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User account was not found"
                        )
                );
    }
}
