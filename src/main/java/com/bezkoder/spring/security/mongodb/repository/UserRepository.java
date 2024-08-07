package com.bezkoder.spring.security.mongodb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bezkoder.spring.security.mongodb.models.User;

public interface UserRepository extends MongoRepository<User, String> {
  List<User> findByUsernameIn(List<String> userNames);

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);
  public User findByEmail(String UserEmail);


  Boolean existsByEmail(String email);

  User findByVerificationToken(String token);

}
