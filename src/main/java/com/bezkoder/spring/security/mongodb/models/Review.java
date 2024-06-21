package com.bezkoder.spring.security.mongodb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;

@Data
@Document(collection = "reviews")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private String id;
    private String serviceName;
    private String userName;
    private List<QuestionAnswer> questions;
    private int score;
    private LocalDateTime timestamp;

}


