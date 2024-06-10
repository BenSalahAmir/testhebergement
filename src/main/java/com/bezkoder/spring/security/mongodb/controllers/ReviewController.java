package com.bezkoder.spring.security.mongodb.controllers;

import com.bezkoder.spring.security.mongodb.models.Review;
import com.bezkoder.spring.security.mongodb.security.services.EmailServiceImpl;
import com.bezkoder.spring.security.mongodb.security.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private EmailServiceImpl emailService;

    @GetMapping("/service/{serviceId}")
    public List<Review> getReviewsByServiceId(@PathVariable String serviceId) {
        return reviewService.getReviewsByServiceId(serviceId);
    }

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        emailService.sendReviewNotification(review.getUserId(),review.getServiceId(),"marketing@elaa-international.com");
        return reviewService.addReview(review);
    }
}
