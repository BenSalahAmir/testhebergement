package com.bezkoder.spring.security.mongodb.models;

import lombok.Data;

@Data
public class QuestionAnswer {
    private String question;
    private String answer;
    private int score;
}