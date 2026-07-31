package com.chefcourier.config;

import com.chefcourier.entity.*;
import com.chefcourier.enums.*;
import com.chefcourier.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalTime;

@Configuration
public class DemoDataSeeder {

    @Bean
    public CommandLineRunner seedDemoData(

            @Value("${app.seed-demo}")
            boolean enabled,

            UserRepository userRepository,
            AddressRepository addressRepository,
            RestaurantRepository restaurantRepository,
            MenuCategoryRepository categoryRepository,
            MenuItemRepository menuItemRepository,
            PasswordEncoder passwordEncoder

    ) {
        return args -> {

            if (!enabled
                    || userRepository
                    .existsByEmailIgnoreCase(
                            "admin@chefcourier.com"
                    )) {

                return;
            }

            User admin = createUser(
                    userRepository,
                    passwordEncoder,
                    "ChefCourier Admin",
                    "admin@chefcourier.com",
                    "9000000001",
                    Role.ADMIN
            );

            User customer = createUser(
                    userRepository,
                    passwordEncoder,
                    "Demo Customer",
                    "customer@chefcourier.com",
                    "9000000002",
                    Role.CUSTOMER
            );

            User owner = createUser(
                    userRepository,
                    passwordEncoder,
                    "Demo Chef",
                    "owner@chefcourier.com",
                    "9000000003",
                    Role.RESTAURANT_OWNER
            );

            createUser(
                    userRepository,
                    passwordEncoder,
                    "Demo Courier",
                    "delivery@chefcourier.com",
                    "9000000004",
                    Role.DELIVERY_PARTNER
            );

            Address address = new Address();

            address.setUser(customer);
            address.setLabel("Home");
            address.setAddressLine1(
                    "1 Demo Street"
            );
            address.setCity("Hyderabad");
            address.setState("Telangana");
            address.setPostalCode("500001");
            address.setCountry("India");
            address.setDefaultAddress(true);

            addressRepository.save(address);

            Restaurant restaurant =
                    new Restaurant();

            restaurant.setOwner(owner);

            restaurant.setName(
                    "Spice Route Kitchen"
            );

            restaurant.setDescription(
                    "Fresh Indian comfort food"
            );

            restaurant.setCuisineType(
                    "Indian"
            );

            restaurant.setPhone(
                    "9000000010"
            );

            restaurant.setAddress(
                    "10 Food Street, Hyderabad"
            );

            restaurant.setCity(
                    "Hyderabad"
            );

            restaurant.setOpeningTime(
                    LocalTime.of(9, 0)
            );

            restaurant.setClosingTime(
                    LocalTime.of(23, 0)
            );

            restaurant.setStatus(
                    RestaurantStatus.APPROVED
            );

            restaurant.setActive(true);

            restaurant =
                    restaurantRepository.save(
                            restaurant
                    );

            MenuCategory category =
                    new MenuCategory();

            category.setRestaurant(
                    restaurant
            );

            category.setName(
                    "Main Course"
            );

            category =
                    categoryRepository.save(
                            category
                    );

            createMenuItem(
                    menuItemRepository,
                    restaurant,
                    category,
                    "Paneer Bowl",
                    "Paneer, rice and vegetables",
                    new BigDecimal("249"),
                    true
            );

            createMenuItem(
                    menuItemRepository,
                    restaurant,
                    category,
                    "Chicken Biryani",
                    "Aromatic rice and chicken",
                    new BigDecimal("299"),
                    false
            );
        };
    }

    private User createUser(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            String fullName,
            String email,
            String phone,
            Role role
    ) {
        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phone);

        user.setPassword(
                passwordEncoder.encode(
                        "Password@123"
                )
        );

        user.setRole(role);
        user.setEnabled(true);

        return repository.save(user);
    }

    private void createMenuItem(
            MenuItemRepository repository,
            Restaurant restaurant,
            MenuCategory category,
            String name,
            String description,
            BigDecimal price,
            boolean vegetarian
    ) {
        MenuItem item = new MenuItem();

        item.setRestaurant(restaurant);
        item.setCategory(category);
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setVegetarian(vegetarian);
        item.setAvailable(true);

        item.setPreparationMinutes(
                25
        );

        repository.save(item);
    }
}
