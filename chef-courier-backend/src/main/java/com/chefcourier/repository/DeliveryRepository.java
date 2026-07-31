package com.chefcourier.repository;

import com.chefcourier.entity.Delivery;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface DeliveryRepository
        extends JpaRepository<Delivery, Long> {

    @EntityGraph(
            attributePaths = {
                    "order",
                    "order.restaurant",
                    "order.customer",
                    "deliveryPartner"
            }
    )
    List<Delivery>
    findByDeliveryPartnerIdOrderByAssignedAtDesc(
            Long partnerId
    );

    @EntityGraph(
            attributePaths = {
                    "order",
                    "order.restaurant",
                    "order.customer",
                    "deliveryPartner"
            }
    )
    Optional<Delivery>
    findByIdAndDeliveryPartnerId(
            Long deliveryId,
            Long partnerId
    );

    Optional<Delivery> findByOrderId(
            Long orderId
    );
}
