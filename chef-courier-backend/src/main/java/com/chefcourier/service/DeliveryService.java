package com.chefcourier.service;

import com.chefcourier.dto.response.DeliveryResponse;
import com.chefcourier.entity.*;
import com.chefcourier.enums.*;
import com.chefcourier.exception.*;
import com.chefcourier.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository
            deliveryRepository;

    private final OrderRepository
            orderRepository;

    private final UserRepository
            userRepository;

    private final UserLookupService
            userLookupService;

    private final MapperService
            mapperService;

    public DeliveryService(
            DeliveryRepository deliveryRepository,
            OrderRepository orderRepository,
            UserRepository userRepository,
            UserLookupService userLookupService,
            MapperService mapperService
    ) {
        this.deliveryRepository =
                deliveryRepository;

        this.orderRepository =
                orderRepository;

        this.userRepository =
                userRepository;

        this.userLookupService =
                userLookupService;

        this.mapperService =
                mapperService;
    }

    @Transactional
    public DeliveryResponse assignPartner(
            Long orderId,
            Long partnerId
    ) {
        CustomerOrder order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order was not found"
                                )
                        );

        if (order.getStatus()
                != OrderStatus.READY_FOR_PICKUP) {

            throw new AppException(
                    "Order is not ready for pickup"
            );
        }

        if (deliveryRepository
                .findByOrderId(orderId)
                .isPresent()) {

            throw new AppException(
                    "Delivery has already been assigned"
            );
        }

        User partner =
                userRepository
                        .findById(partnerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Delivery partner was not found"
                                )
                        );

        if (partner.getRole()
                != Role.DELIVERY_PARTNER
                || !partner.isEnabled()) {

            throw new AppException(
                    "Selected user is not an active delivery partner"
            );
        }

        Delivery delivery =
                new Delivery();

        delivery.setOrder(order);

        delivery.setDeliveryPartner(
                partner
        );

        delivery.setStatus(
                DeliveryStatus.ASSIGNED
        );

        delivery.setAssignedAt(
                LocalDateTime.now()
        );

        return mapperService.delivery(
                deliveryRepository.save(
                        delivery
                )
        );
    }

    public List<DeliveryResponse> getTasks(
            String email
    ) {
        User partner =
                userLookupService.getByEmail(
                        email
                );

        return deliveryRepository
                .findByDeliveryPartnerIdOrderByAssignedAtDesc(
                        partner.getId()
                )
                .stream()
                .map(mapperService::delivery)
                .toList();
    }

    @Transactional
    public DeliveryResponse updateStatus(
            String email,
            Long deliveryId,
            DeliveryStatus nextStatus
    ) {
        User partner =
                userLookupService.getByEmail(
                        email
                );

        Delivery delivery =
                deliveryRepository
                        .findByIdAndDeliveryPartnerId(
                                deliveryId,
                                partner.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Delivery was not found"
                                )
                        );

        DeliveryStatus current =
                delivery.getStatus();

        boolean valid =
                current == DeliveryStatus.ASSIGNED
                        && nextStatus
                        == DeliveryStatus.ACCEPTED
                        || current
                        == DeliveryStatus.ACCEPTED
                        && nextStatus
                        == DeliveryStatus.PICKED_UP
                        || current
                        == DeliveryStatus.PICKED_UP
                        && nextStatus
                        == DeliveryStatus.OUT_FOR_DELIVERY
                        || current
                        == DeliveryStatus.OUT_FOR_DELIVERY
                        && nextStatus
                        == DeliveryStatus.DELIVERED;

        if (!valid) {
            throw new AppException(
                    "Invalid delivery status transition"
            );
        }

        delivery.setStatus(nextStatus);

        CustomerOrder order =
                delivery.getOrder();

        if (nextStatus
                == DeliveryStatus.PICKED_UP) {

            delivery.setPickedUpAt(
                    LocalDateTime.now()
            );

            order.setStatus(
                    OrderStatus.PICKED_UP
            );
        }

        if (nextStatus
                == DeliveryStatus.OUT_FOR_DELIVERY) {

            order.setStatus(
                    OrderStatus.OUT_FOR_DELIVERY
            );
        }

        if (nextStatus
                == DeliveryStatus.DELIVERED) {

            delivery.setDeliveredAt(
                    LocalDateTime.now()
            );

            order.setStatus(
                    OrderStatus.DELIVERED
            );

            if (order.getPaymentMethod()
                    == PaymentMethod.COD) {

                order.setPaymentStatus(
                        PaymentStatus.SUCCESS
                );
            }
        }

        orderRepository.save(order);

        return mapperService.delivery(
                deliveryRepository.save(
                        delivery
                )
        );
    }
}
