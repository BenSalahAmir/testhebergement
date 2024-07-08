package com.bezkoder.spring.security.mongodb.repository;

import com.bezkoder.spring.security.mongodb.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    @Query("{ 'serviceName' : { $regex: ?0, $options: 'i' } }")

    List<Review> findByServiceName(String serviceName);

    long countByServiceName(String serviceName);


    List<Review> findByUserNameIn(List<String> userNames);



}