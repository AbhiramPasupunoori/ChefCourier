package com.chefcourier.service;

import com.chefcourier.dto.request.*;
import com.chefcourier.dto.response.*;
import com.chefcourier.entity.User;
import com.chefcourier.enums.Role;
import com.chefcourier.exception.AppException;
import com.chefcourier.repository.UserRepository;
import com.chefcourier.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MapperService mapperService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            MapperService mapperService
    ) {
        this.userRepository =
                userRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.jwtService =
                jwtService;

        this.mapperService =
                mapperService;
    }

    @Transactional
    public AuthResponse register(
            RegisterRequest request
    ) {
        String email =
                request.email()
                        .trim()
                        .toLowerCase(Locale.ROOT);

        if (userRepository
                .existsByEmailIgnoreCase(email)) {

            throw new AppException(
                    "Email is already registered"
            );
        }

        if (request.role() == Role.ADMIN) {
            throw new AppException(
                    "Admin accounts cannot be registered publicly"
            );
        }

        User user = new User();

        user.setFullName(
                request.fullName().trim()
        );

        user.setEmail(email);

        user.setPassword(
                passwordEncoder.encode(
                        request.password()
                )
        );

        user.setPhoneNumber(
                request.phoneNumber().trim()
        );

        user.setRole(request.role());
        user.setEnabled(true);

        User savedUser =
                userRepository.save(user);

        return createResponse(savedUser);
    }

    public AuthResponse login(
            LoginRequest request
    ) {
        User user = userRepository
                .findByEmailIgnoreCase(
                        request.email().trim()
                )
                .orElseThrow(() ->
                        new AppException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new AppException(
                    "Invalid email or password"
            );
        }

        if (!user.isEnabled()) {
            throw new AppException(
                    "User account is disabled"
            );
        }

        return createResponse(user);
    }

    public UserResponse getCurrentUser(
            String email
    ) {
        User user = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new AppException(
                                "User was not found"
                        )
                );

        return mapperService.user(user);
    }

    private AuthResponse createResponse(
            User user
    ) {
        return new AuthResponse(
                jwtService.generateToken(user),
                "Bearer",
                jwtService.getExpirationSeconds(),
                mapperService.user(user)
        );
    }
}
