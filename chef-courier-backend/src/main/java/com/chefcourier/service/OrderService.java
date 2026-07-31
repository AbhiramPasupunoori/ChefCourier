package com.chefcourier.service;

import com.chefcourier.dto.request.PlaceOrderRequest;
import com.chefcourier.dto.response.OrderResponse;
import com.chefcourier.entity.*;
import com.chefcourier.enums.*;
import com.chefcourier.exception.*;
import com.chefcourier.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final UserLookupService userLookupService;
    private final CartService cartService;
    private final RestaurantService restaurantService;
    private final MapperService mapperService;

    public OrderService(
            OrderRepository orderRepository,
            AddressRepository addressRepository,
            UserLookupService userLookupService,
            CartService cartService,
            RestaurantService restaurantService,
            MapperService mapperService
    ) {
        this.orderRepository =
                orderRepository;

        this.addressRepository =
                addressRepository;

        this.userLookupService =
                userLookupService;

        this.cartService =
                cartService;

        this.restaurantService =
                restaurantService;

        this.mapperService =
                mapperService;
    }

    @Transactional
    public OrderResponse placeOrder(
            String email,
            PlaceOrderRequest request
    ) {
        User customer =
                userLookupService.getByEmail(
                        email
                );

        Cart cart =
                cartService.getCartEntity(
                        email
                );

        if (cart.getItems().isEmpty()) {
            throw new AppException(
                    "Cart is empty"
            );
        }

        Address address =
                addressRepository
                        .findByIdAndUserId(
                                request.addressId(),
                                customer.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address was not found"
                                )
                        );

        BigDecimal subtotal =
                cart.getItems()
                        .stream()
                        .map(item ->
                                item.getUnitPrice()
                                        .multiply(
                                                BigDecimal.valueOf(
                                                        item.getQuantity()
                                                )
                                        )
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal deliveryFee =
                subtotal.compareTo(
                        new BigDecimal("500")
                ) >= 0
                        ? BigDecimal.ZERO
                        : new BigDecimal("49");

        CustomerOrder order =
                new CustomerOrder();

        order.setCustomer(customer);
        order.setRestaurant(
                cart.getRestaurant()
        );

        order.setDeliveryAddress(
                address.formattedAddress()
        );

        order.setPaymentMethod(
                request.paymentMethod()
        );

        order.setPaymentStatus(
                PaymentStatus.PENDING
        );

        order.setStatus(
                request.paymentMethod()
                        == PaymentMethod.ONLINE
                        ? OrderStatus.PENDING_PAYMENT
                        : OrderStatus.PLACED
        );

        order.setSubtotal(subtotal);
        order.setDeliveryFee(deliveryFee);

        order.setTotalAmount(
                subtotal.add(deliveryFee)
        );

        order.setSpecialInstructions(
                request.specialInstructions()
        );

        for (CartItem cartItem
                : cart.getItems()) {

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(order);

            orderItem.setMenuItemId(
                    cartItem.getMenuItem().getId()
            );

            orderItem.setItemName(
                    cartItem.getMenuItem().getName()
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setUnitPrice(
                    cartItem.getUnitPrice()
            );

            orderItem.setTotalPrice(
                    cartItem.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            cartItem.getQuantity()
                                    )
                            )
            );

            order.getItems().add(orderItem);
        }

        CustomerOrder savedOrder =
                orderRepository.save(order);

        cartService.clearCart(cart);

        return mapperService.order(
                savedOrder
        );
    }

    public List<OrderResponse>
    customerOrders(
            String email
    ) {
        User customer =
                userLookupService.getByEmail(
                        email
                );

        return orderRepository
                .findByCustomerIdOrderByCreatedAtDesc(
                        customer.getId()
                )
                .stream()
                .map(mapperService::order)
                .toList();
    }

    @Transactional
    public OrderResponse payOrder(
            String email,
            Long orderId
    ) {
        User customer =
                userLookupService.getByEmail(
                        email
                );

        CustomerOrder order =
                orderRepository
                        .findByIdAndCustomerId(
                                orderId,
                                customer.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order was not found"
                                )
                        );

        if (order.getPaymentMethod()
                != PaymentMethod.ONLINE
                || order.getStatus()
                != OrderStatus.PENDING_PAYMENT) {

            throw new AppException(
                    "Order is not awaiting online payment"
            );
        }

        /*
         * Development-only simulated payment.
         * Replace with Razorpay or Stripe verification
         * before production deployment.
         */
        order.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        order.setStatus(
                OrderStatus.PLACED
        );

        return mapperService.order(
                orderRepository.save(order)
        );
    }

    @Transactional
    public OrderResponse cancelOrder(
            String email,
            Long orderId
    ) {
        User customer =
                userLookupService.getByEmail(
                        email
                );

        CustomerOrder order =
                orderRepository
                        .findByIdAndCustomerId(
                                orderId,
                                customer.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order was not found"
                                )
                        );

        boolean cancellable =
                order.getStatus()
                        == OrderStatus.PENDING_PAYMENT
                        || order.getStatus()
                        == OrderStatus.PLACED
                        || order.getStatus()
                        == OrderStatus.CONFIRMED;

        if (!cancellable) {
            throw new AppException(
                    "Order can no longer be cancelled"
            );
        }

        order.setStatus(
                OrderStatus.CANCELLED
        );

        return mapperService.order(
                orderRepository.save(order)
        );
    }

    public List<OrderResponse> ownerOrders(
            String email,
            Long restaurantId
    ) {
        restaurantService.getOwnedRestaurant(
                email,
                restaurantId
        );

        return orderRepository
                .findByRestaurantIdOrderByCreatedAtDesc(
                        restaurantId
                )
                .stream()
                .map(mapperService::order)
                .toList();
    }

    @Transactional
    public OrderResponse ownerUpdateStatus(
            String email,
            Long restaurantId,
            Long orderId,
            OrderStatus nextStatus
    ) {
        restaurantService.getOwnedRestaurant(
                email,
                restaurantId
        );

        CustomerOrder order =
                orderRepository
                        .findByIdAndRestaurantId(
                                orderId,
                                restaurantId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order was not found"
                                )
                        );

        OrderStatus current =
                order.getStatus();

        boolean valid =
                current == OrderStatus.PLACED
                        && (
                        nextStatus
                                == OrderStatus.CONFIRMED
                                || nextStatus
                                == OrderStatus.REJECTED
                )
                        || current
                        == OrderStatus.CONFIRMED
                        && nextStatus
                        == OrderStatus.PREPARING
                        || current
                        == OrderStatus.PREPARING
                        && nextStatus
                        == OrderStatus.READY_FOR_PICKUP;

        if (!valid) {
            throw new AppException(
                    "Invalid order status transition"
            );
        }

        order.setStatus(nextStatus);

        return mapperService.order(
                orderRepository.save(order)
        );
    }

    public List<OrderResponse> adminOrders(
            OrderStatus status
    ) {
        List<CustomerOrder> orders =
                status == null
                        ? orderRepository.findAll()
                        : orderRepository
                        .findByStatusOrderByCreatedAtDesc(
                                status
                        );

        return orders.stream()
                .map(mapperService::order)
                .toList();
    }
}
