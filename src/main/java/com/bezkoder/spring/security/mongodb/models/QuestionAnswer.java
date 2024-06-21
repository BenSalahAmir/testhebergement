package com.bezkoder.spring.security.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswer {
    private String question;
    private String answer;
    private int score;
}