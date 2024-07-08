package com.bezkoder.spring.security.mongodb.security.services;

import com.bezkoder.spring.security.mongodb.models.ContratAssurance;
import com.bezkoder.spring.security.mongodb.models.User;
import com.bezkoder.spring.security.mongodb.repository.ContratAssuranceRepository;
import com.bezkoder.spring.security.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private ContratAssuranceRepository contratAssuranceRepository;



    public User activateUser(String token) {
        User user = userDao.findByVerificationToken(token);
        if (user != null) {
            user.setIsverified(1);
            user.setVerificationToken(null);
            userDao.save(user);
        }
        return user;
    }
    public User findByUserEmail(String UserEmail)
    {
        return this.userDao.findByEmail(UserEmail);
    }

    public User updateuser(User u){

        return userDao.save(u);
    }


    public List<User> getUsersForGATContracts() {
        // Fetch contracts where compagnieAssurance is GAT
        List<ContratAssurance> gatContracts = contratAssuranceRepository.findByCompagnieAssurance("GAT");

        // Extract usernames from these contracts
        List<String> userNames = gatContracts.stream()
                .map(ContratAssurance::getNomAssure)
                .collect(Collectors.toList());

        // Fetch users with these usernames
        return userDao.findByUsernameIn(userNames);
    }






}
