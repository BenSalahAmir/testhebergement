package com.bezkoder.spring.security.mongodb.security.services;

import com.bezkoder.spring.security.mongodb.models.QuestionAnswer;
import com.bezkoder.spring.security.mongodb.models.Review;
import com.bezkoder.spring.security.mongodb.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewsByServiceId(String serviceId) {
        return reviewRepository.findByServiceId(serviceId);
    }

    public Review addReview(Review review) {
        int totalScore = review.getQuestions().stream().mapToInt(QuestionAnswer::getScore).sum();
        review.setScore(totalScore);
        review.setTimestamp(LocalDateTime.now());
        return reviewRepository.save(review);
    }
}
