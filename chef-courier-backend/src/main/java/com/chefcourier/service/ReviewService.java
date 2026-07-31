package com.chefcourier.service;

import com.chefcourier.dto.request.ReviewRequest;
import com.chefcourier.dto.response.ReviewResponse;
import com.chefcourier.entity.*;
import com.chefcourier.enums.OrderStatus;
import com.chefcourier.exception.*;
import com.chefcourier.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository
            reviewRepository;

    private final OrderRepository
            orderRepository;

    private final RestaurantRepository
            restaurantRepository;

    private final UserLookupService
            userLookupService;

    private final MapperService
            mapperService;

    public ReviewService(
            ReviewRepository reviewRepository,
            OrderRepository orderRepository,
            RestaurantRepository restaurantRepository,
            UserLookupService userLookupService,
            MapperService mapperService
    ) {
        this.reviewRepository =
                reviewRepository;

        this.orderRepository =
                orderRepository;

        this.restaurantRepository =
                restaurantRepository;

        this.userLookupService =
                userLookupService;

        this.mapperService =
                mapperService;
    }

    @Transactional
    public ReviewResponse createReview(
            String email,
            Long orderId,
            ReviewRequest request
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

        if (order.getStatus()
                != OrderStatus.DELIVERED) {

            throw new AppException(
                    "Only delivered orders can be reviewed"
            );
        }

        if (reviewRepository
                .existsByOrderId(orderId)) {

            throw new AppException(
                    "Order has already been reviewed"
            );
        }

        Review review =
                new Review();

        review.setOrder(order);
        review.setCustomer(customer);

        review.setRestaurant(
                order.getRestaurant()
        );

        review.setRating(
                request.rating()
        );

        review.setComment(
                request.comment().trim()
        );

        Review savedReview =
                reviewRepository.save(review);

        Double average =
                reviewRepository
                        .averageForRestaurant(
                                order.getRestaurant()
                                        .getId()
                        );

        order.getRestaurant()
                .setAverageRating(
                        average == null
                                ? 0
                                : average
                );

        restaurantRepository.save(
                order.getRestaurant()
        );

        return mapperService.review(
                savedReview
        );
    }

    public List<ReviewResponse> listReviews(
            Long restaurantId
    ) {
        return reviewRepository
                .findByRestaurantIdOrderByCreatedAtDesc(
                        restaurantId
                )
                .stream()
                .map(mapperService::review)
                .toList();
    }
}
