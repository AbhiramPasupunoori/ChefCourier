package com.chefcourier.config;

import com.chefcourier.entity.Address;
import com.chefcourier.entity.MenuCategory;
import com.chefcourier.entity.MenuItem;
import com.chefcourier.entity.Restaurant;
import com.chefcourier.entity.User;
import com.chefcourier.enums.RestaurantStatus;
import com.chefcourier.enums.Role;
import com.chefcourier.repository.AddressRepository;
import com.chefcourier.repository.MenuCategoryRepository;
import com.chefcourier.repository.MenuItemRepository;
import com.chefcourier.repository.RestaurantRepository;
import com.chefcourier.repository.UserRepository;
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

            @Value("${app.seed-demo:true}")
            boolean seedDemo,

            UserRepository userRepository,

            AddressRepository addressRepository,

            RestaurantRepository restaurantRepository,

            MenuCategoryRepository categoryRepository,

            MenuItemRepository menuItemRepository,

            PasswordEncoder passwordEncoder

    ) {
        return args -> {

            if (!seedDemo) {
                return;
            }

            createUser(
                    userRepository,
                    passwordEncoder,
                    "ChefCourier Admin",
                    "admin@chefcourier.com",
                    "9000000001",
                    Role.ADMIN
            );

            User customer =
                    createUser(
                            userRepository,
                            passwordEncoder,
                            "Demo Customer",
                            "customer@chefcourier.com",
                            "9000000002",
                            Role.CUSTOMER
                    );

            createCustomerAddress(
                    addressRepository,
                    customer
            );

            User indianOwner =
                    createUser(
                            userRepository,
                            passwordEncoder,
                            "Arjun Rao",
                            "indian.owner@chefcourier.com",
                            "9000000003",
                            Role.RESTAURANT_OWNER
                    );

            User pizzaOwner =
                    createUser(
                            userRepository,
                            passwordEncoder,
                            "Marco Joseph",
                            "pizza.owner@chefcourier.com",
                            "9000000004",
                            Role.RESTAURANT_OWNER
                    );

            User asianOwner =
                    createUser(
                            userRepository,
                            passwordEncoder,
                            "Mei Sharma",
                            "asian.owner@chefcourier.com",
                            "9000000005",
                            Role.RESTAURANT_OWNER
                    );

            User healthyOwner =
                    createUser(
                            userRepository,
                            passwordEncoder,
                            "Riya Kapoor",
                            "healthy.owner@chefcourier.com",
                            "9000000006",
                            Role.RESTAURANT_OWNER
                    );

            createUser(
                    userRepository,
                    passwordEncoder,
                    "Demo Courier",
                    "delivery@chefcourier.com",
                    "9000000007",
                    Role.DELIVERY_PARTNER
            );

            Restaurant indianRestaurant =
                    createRestaurant(
                            restaurantRepository,
                            indianOwner,
                            "Spice Route Kitchen",
                            "Authentic Indian curries, biryani and freshly prepared breads.",
                            "Indian",
                            "9010000001",
                            "Road Number 12, Banjara Hills",
                            "Hyderabad",
                            "https://placehold.co/900x600/F97316/FFFFFF?text=Spice+Route+Kitchen"
                    );

            Restaurant pizzaRestaurant =
                    createRestaurant(
                            restaurantRepository,
                            pizzaOwner,
                            "Urban Pizza Co.",
                            "Hand-tossed pizzas, garlic breads and classic Italian desserts.",
                            "Italian",
                            "9010000002",
                            "Jubilee Hills Check Post",
                            "Hyderabad",
                            "https://placehold.co/900x600/C2410C/FFFFFF?text=Urban+Pizza+Co"
                    );

            Restaurant asianRestaurant =
                    createRestaurant(
                            restaurantRepository,
                            asianOwner,
                            "Wok and Roll",
                            "Asian-inspired noodles, fried rice, starters and spicy wok dishes.",
                            "Asian",
                            "9010000003",
                            "Hitech City Main Road",
                            "Hyderabad",
                            "https://placehold.co/900x600/1C1917/F97316?text=Wok+and+Roll"
                    );

            Restaurant healthyRestaurant =
                    createRestaurant(
                            restaurantRepository,
                            healthyOwner,
                            "Green Bowl Cafe",
                            "Healthy bowls, fresh breakfasts, smoothies and protein-rich meals.",
                            "Healthy",
                            "9010000004",
                            "Gachibowli Financial District",
                            "Hyderabad",
                            "https://placehold.co/900x600/15803D/FFFFFF?text=Green+Bowl+Cafe"
                    );

            seedIndianMenu(
                    categoryRepository,
                    menuItemRepository,
                    indianRestaurant
            );

            seedPizzaMenu(
                    categoryRepository,
                    menuItemRepository,
                    pizzaRestaurant
            );

            seedAsianMenu(
                    categoryRepository,
                    menuItemRepository,
                    asianRestaurant
            );

            seedHealthyMenu(
                    categoryRepository,
                    menuItemRepository,
                    healthyRestaurant
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
        return repository
                .findByEmailIgnoreCase(email)
                .orElseGet(() -> {

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
                });
    }

    private void createCustomerAddress(

            AddressRepository addressRepository,

            User customer

    ) {
        boolean hasAddress =
                !addressRepository
                        .findAllByUserIdOrderByDefaultAddressDescCreatedAtDesc(
                                customer.getId()
                        )
                        .isEmpty();

        if (hasAddress) {
            return;
        }

        Address address =
                new Address();

        address.setUser(customer);
        address.setLabel("Home");

        address.setAddressLine1(
                "1 Demo Street"
        );

        address.setAddressLine2(
                "Near Metro Station"
        );

        address.setCity(
                "Hyderabad"
        );

        address.setState(
                "Telangana"
        );

        address.setPostalCode(
                "500001"
        );

        address.setCountry(
                "India"
        );

        address.setDefaultAddress(
                true
        );

        addressRepository.save(address);
    }

    private Restaurant createRestaurant(

            RestaurantRepository repository,

            User owner,

            String name,

            String description,

            String cuisine,

            String phone,

            String address,

            String city,

            String imageUrl

    ) {
        return repository
                .findByNameIgnoreCase(name)
                .orElseGet(() -> {

                    Restaurant restaurant =
                            new Restaurant();

                    restaurant.setOwner(owner);
                    restaurant.setName(name);

                    restaurant.setDescription(
                            description
                    );

                    restaurant.setCuisineType(
                            cuisine
                    );

                    restaurant.setPhone(phone);

                    restaurant.setAddress(
                            address
                    );

                    restaurant.setCity(city);

                    restaurant.setImageUrl(
                            imageUrl
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

                    return repository.save(
                            restaurant
                    );
                });
    }

    private MenuCategory createCategory(

            MenuCategoryRepository repository,

            Restaurant restaurant,

            String name

    ) {
        return repository
                .findByRestaurantIdAndNameIgnoreCase(
                        restaurant.getId(),
                        name
                )
                .orElseGet(() -> {

                    MenuCategory category =
                            new MenuCategory();

                    category.setRestaurant(
                            restaurant
                    );

                    category.setName(name);

                    return repository.save(
                            category
                    );
                });
    }

    private void createMenuItem(

            MenuItemRepository repository,

            Restaurant restaurant,

            MenuCategory category,

            String name,

            String description,

            BigDecimal price,

            boolean vegetarian,

            String imageUrl,

            int preparationMinutes

    ) {
        boolean exists =
                repository
                        .existsByRestaurantIdAndNameIgnoreCase(
                                restaurant.getId(),
                                name
                        );

        if (exists) {
            return;
        }

        MenuItem item =
                new MenuItem();

        item.setRestaurant(restaurant);
        item.setCategory(category);
        item.setName(name);

        item.setDescription(
                description
        );

        item.setPrice(price);

        item.setVegetarian(
                vegetarian
        );

        item.setAvailable(true);

        item.setImageUrl(
                imageUrl
        );

        item.setPreparationMinutes(
                preparationMinutes
        );

        repository.save(item);
    }

    private void seedIndianMenu(

            MenuCategoryRepository categoryRepository,

            MenuItemRepository menuItemRepository,

            Restaurant restaurant

    ) {
        MenuCategory biryani =
                createCategory(
                        categoryRepository,
                        restaurant,
                        "Biryani"
                );

        MenuCategory curries =
                createCategory(
                        categoryRepository,
                        restaurant,
                        "Curries and Breads"
                );

        createMenuItem(
                menuItemRepository,
                restaurant,
                biryani,
                "Chicken Biryani",
                "Aromatic basmati rice cooked with spiced chicken.",
                new BigDecimal("299.00"),
                false,
                "https://placehold.co/900x600/F97316/FFFFFF?text=Chicken+Biryani",
                30
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                biryani,
                "Paneer Biryani",
                "Fragrant rice layered with paneer and Indian spices.",
                new BigDecimal("259.00"),
                true,
                "https://placehold.co/900x600/F97316/FFFFFF?text=Paneer+Biryani",
                25
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                curries,
                "Butter Chicken",
                "Tender chicken cooked in a creamy tomato gravy.",
                new BigDecimal("329.00"),
                false,
                "https://placehold.co/900x600/C2410C/FFFFFF?text=Butter+Chicken",
                25
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                curries,
                "Garlic Naan",
                "Soft tandoor-baked naan topped with garlic and butter.",
                new BigDecimal("79.00"),
                true,
                "https://placehold.co/900x600/FED7AA/7C2D12?text=Garlic+Naan",
                15
        );
    }

    private void seedPizzaMenu(

            MenuCategoryRepository categoryRepository,

            MenuItemRepository menuItemRepository,

            Restaurant restaurant

    ) {
        MenuCategory pizzas =
                createCategory(
                        categoryRepository,
                        restaurant,
                        "Pizzas"
                );

        MenuCategory sides =
                createCategory(
                        categoryRepository,
                        restaurant,
                        "Sides and Desserts"
                );

        createMenuItem(
                menuItemRepository,
                restaurant,
                pizzas,
                "Margherita Pizza",
                "Classic pizza with mozzarella, tomato and fresh basil.",
                new BigDecimal("299.00"),
                true,
                "https://placehold.co/900x600/C2410C/FFFFFF?text=Margherita+Pizza",
                25
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                pizzas,
                "Farmhouse Pizza",
                "Loaded with onion, capsicum, mushroom and tomato.",
                new BigDecimal("399.00"),
                true,
                "https://placehold.co/900x600/C2410C/FFFFFF?text=Farmhouse+Pizza",
                30
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                sides,
                "Cheesy Garlic Bread",
                "Oven-baked garlic bread topped with melted cheese.",
                new BigDecimal("179.00"),
                true,
                "https://placehold.co/900x600/FED7AA/7C2D12?text=Garlic+Bread",
                15
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                sides,
                "Tiramisu",
                "Coffee-flavoured Italian dessert with mascarpone.",
                new BigDecimal("219.00"),
                true,
                "https://placehold.co/900x600/1C1917/F97316?text=Tiramisu",
                10
        );
    }

    private void seedAsianMenu(

            MenuCategoryRepository categoryRepository,

            MenuItemRepository menuItemRepository,

            Restaurant restaurant

    ) {
        MenuCategory mains =
                createCategory(
                        categoryRepository,
                        restaurant,
                        "Noodles and Rice"
                );

        MenuCategory starters =
                createCategory(
                        categoryRepository,
                        restaurant,
                        "Asian Starters"
                );

        createMenuItem(
                menuItemRepository,
                restaurant,
                mains,
                "Vegetable Hakka Noodles",
                "Stir-fried noodles with vegetables and Asian sauces.",
                new BigDecimal("229.00"),
                true,
                "https://placehold.co/900x600/1C1917/F97316?text=Hakka+Noodles",
                20
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                mains,
                "Chicken Fried Rice",
                "Wok-tossed rice with chicken, egg and vegetables.",
                new BigDecimal("259.00"),
                false,
                "https://placehold.co/900x600/1C1917/F97316?text=Chicken+Fried+Rice",
                22
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                starters,
                "Chilli Paneer",
                "Crispy paneer tossed with peppers and chilli sauce.",
                new BigDecimal("249.00"),
                true,
                "https://placehold.co/900x600/C2410C/FFFFFF?text=Chilli+Paneer",
                20
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                starters,
                "Vegetable Spring Rolls",
                "Crispy rolls filled with seasoned vegetables.",
                new BigDecimal("179.00"),
                true,
                "https://placehold.co/900x600/F97316/FFFFFF?text=Spring+Rolls",
                15
        );
    }

    private void seedHealthyMenu(

            MenuCategoryRepository categoryRepository,

            MenuItemRepository menuItemRepository,

            Restaurant restaurant

    ) {
        MenuCategory bowls =
                createCategory(
                        categoryRepository,
                        restaurant,
                        "Healthy Bowls"
                );

        MenuCategory breakfast =
                createCategory(
                        categoryRepository,
                        restaurant,
                        "Breakfast and Drinks"
                );

        createMenuItem(
                menuItemRepository,
                restaurant,
                bowls,
                "Grilled Chicken Protein Bowl",
                "Grilled chicken, brown rice, vegetables and yoghurt dressing.",
                new BigDecimal("349.00"),
                false,
                "https://placehold.co/900x600/15803D/FFFFFF?text=Protein+Bowl",
                20
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                bowls,
                "Paneer Quinoa Bowl",
                "Quinoa, grilled paneer, greens and fresh vegetables.",
                new BigDecimal("319.00"),
                true,
                "https://placehold.co/900x600/15803D/FFFFFF?text=Paneer+Quinoa+Bowl",
                18
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                breakfast,
                "Avocado Toast",
                "Whole-grain toast with avocado, tomato and herbs.",
                new BigDecimal("229.00"),
                true,
                "https://placehold.co/900x600/15803D/FFFFFF?text=Avocado+Toast",
                12
        );

        createMenuItem(
                menuItemRepository,
                restaurant,
                breakfast,
                "Berry Smoothie",
                "Mixed berries blended with yoghurt and banana.",
                new BigDecimal("189.00"),
                true,
                "https://placehold.co/900x600/15803D/FFFFFF?text=Berry+Smoothie",
                8
        );
    }
}
