package com.bezkoder.spring.security.mongodb.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = Logger.getLogger(FirebaseConfig.class.getName());

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        try (FileInputStream serviceAccount =
                     new FileInputStream("src/main/resources/firebase-service-account.json")) {

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            logger.log(Level.INFO, "Firebase initialized successfully.");
            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error initializing Firebase: " + e.getMessage(), e);
            throw e;
        }
    }
}
