package com.chefcourier.controller;

import com.chefcourier.dto.response.ReviewResponse;
import com.chefcourier.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(
            ReviewService reviewService
    ) {
        this.reviewService =
                reviewService;
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<ReviewResponse> reviews(
            @PathVariable
            Long restaurantId
    ) {
        return reviewService.listReviews(
                restaurantId
        );
    }
}
