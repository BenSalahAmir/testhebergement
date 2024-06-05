package com.bezkoder.spring.security.mongodb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;

@Data
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String serviceId;
    private String userId;
    private List<QuestionAnswer> questions;
    private int score;
    private LocalDateTime timestamp;
}


