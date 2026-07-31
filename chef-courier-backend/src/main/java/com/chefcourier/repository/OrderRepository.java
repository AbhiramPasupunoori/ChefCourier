package com.chefcourier.repository;

import com.chefcourier.entity.CustomerOrder;
import com.chefcourier.enums.OrderStatus;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface OrderRepository
        extends JpaRepository<CustomerOrder, Long> {

    @EntityGraph(
            attributePaths = {
                    "items",
                    "restaurant",
                    "customer"
            }
    )
    List<CustomerOrder>
    findByCustomerIdOrderByCreatedAtDesc(
            Long customerId
    );

    @EntityGraph(
            attributePaths = {
                    "items",
                    "restaurant",
                    "customer"
            }
    )
    Optional<CustomerOrder>
    findByIdAndCustomerId(
            Long orderId,
            Long customerId
    );

    @EntityGraph(
            attributePaths = {
                    "items",
                    "restaurant",
                    "customer"
            }
    )
    List<CustomerOrder>
    findByRestaurantIdOrderByCreatedAtDesc(
            Long restaurantId
    );

    @EntityGraph(
            attributePaths = {
                    "items",
                    "restaurant",
                    "customer"
            }
    )
    Optional<CustomerOrder>
    findByIdAndRestaurantId(
            Long orderId,
            Long restaurantId
    );

    @EntityGraph(
            attributePaths = {
                    "items",
                    "restaurant",
                    "customer"
            }
    )
    List<CustomerOrder>
    findByStatusOrderByCreatedAtDesc(
            OrderStatus status
    );
}
