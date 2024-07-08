package com.bezkoder.spring.security.mongodb.controllers;

import com.bezkoder.spring.security.mongodb.models.QuestionAnswer;
import com.bezkoder.spring.security.mongodb.models.Review;
import com.bezkoder.spring.security.mongodb.security.services.EmailServiceImpl;
import com.bezkoder.spring.security.mongodb.security.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*", maxAge = 3600)

public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private EmailServiceImpl emailService;
/*
    @GetMapping("/service/{serviceId}")
    public List<Review> getReviewsByServiceId(@PathVariable String serviceId) {
        return reviewService.getReviewsByServiceId(serviceId);
    }
*/
    @PostMapping
    public Review addReview(@RequestBody Review review) {
        //emailService.sendReviewNotification(review.getUserId(),review.getServiceId(),"marketing@elaa-international.com");
        return reviewService.addReview(review);
    }

    @GetMapping("/score/{serviceName}")
    public double getAverageScore(@PathVariable String serviceName) {
        return reviewService.getAverageScore(serviceName);
    }

    @GetMapping("/getbyid/{id}")
    public Review getreviewbyid(@PathVariable String id) {
        return reviewService.getReviewbyid(id);
    }

    @GetMapping("/getall")
    public List<Review> getallreview() {
        return reviewService.getallReview();
    }


    @GetMapping("/getscore/{id}")
    public int getScoreById(@PathVariable String id) {
        return reviewService.getScoreById(id);
    }


    @GetMapping("/count/{serviceName}")
    public long countReviews(@PathVariable String serviceName) {
        return reviewService.countReviewsByServiceName(serviceName);
    }

    @GetMapping("/responses/{serviceName}")
    public List<QuestionAnswer> getResponsesForQuestions(@PathVariable String serviceName) {
        return reviewService.getResponsesForQuestions(serviceName);
    }


    @GetMapping("/gat")
    public List<Review> getReviewsForGATContracts() {
        return reviewService.getReviewsForGATContracts();
    }




}
