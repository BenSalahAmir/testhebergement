package com.bezkoder.spring.security.mongodb.security.services;

import com.bezkoder.spring.security.mongodb.models.ContratAssurance;
import com.bezkoder.spring.security.mongodb.models.QuestionAnswer;
import com.bezkoder.spring.security.mongodb.models.Review;
import com.bezkoder.spring.security.mongodb.repository.ContratAssuranceRepository;
import com.bezkoder.spring.security.mongodb.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ContratAssuranceRepository contratAssuranceRepository;
/*
    public List<Review> getReviewsByServiceId(String serviceId) {
        return reviewRepository.findByServiceId(serviceId);
    }
*/
    public Review addReview(Review review) {
        int totalScore = review.getQuestions().stream().mapToInt(QuestionAnswer::getScore).sum();
        review.setScore(totalScore);
        review.setTimestamp(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Review getReviewbyid(String idReview) {

        return reviewRepository.findById(idReview).orElse(null);
    }
    public List<Review> getallReview() {

        return reviewRepository.findAll();
    }

    public int getScoreById(String idReview) {
        System.out.println("Fetching score for review with ID: " + idReview); // Debug statement
        Review review = reviewRepository.findById(idReview).orElse(null);
        if (review != null) {
            return review.getScore();
        }
        return 0; // Or throw an exception if preferred
    }




    public double getAverageScore(String serviceName) {
        List<Review> reviews = reviewRepository.findByServiceName(serviceName);
        if (reviews.isEmpty()) {
            return 0;
        }

        double totalScoreSum = 0;
        int totalQuestions = 0;

        for (Review review : reviews) {
            totalScoreSum += review.getQuestions().stream().mapToInt(QuestionAnswer::getScore).sum();
            totalQuestions += review.getQuestions().size();
        }

        return totalQuestions == 0 ? 0 : (totalScoreSum / totalQuestions);
    }


    public long countReviewsByServiceName(String serviceName) {
        return reviewRepository.countByServiceName(serviceName);
    }


    public List<QuestionAnswer> getResponsesForQuestions(String serviceName) {
        List<Review> reviews = reviewRepository.findByServiceName(serviceName);
        return reviews.stream()
                .flatMap(review -> review.getQuestions().stream())
                .collect(Collectors.toList());
    }


    public List<Review> getReviewsForGATContracts() {
        // Fetch contracts where compagnieAssurance is GAT
        List<ContratAssurance> gatContracts = contratAssuranceRepository.findByCompagnieAssurance("GAT");

        // Extract usernames from these contracts
        List<String> userNames = gatContracts.stream()
                .map(ContratAssurance::getNomAssure)
                .collect(Collectors.toList());

        // Fetch reviews with these usernames
        return reviewRepository.findByUserNameIn(userNames);
    }

}
