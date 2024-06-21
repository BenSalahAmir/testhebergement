package com.bezkoder.spring.security.mongodb.repository;

import com.bezkoder.spring.security.mongodb.models.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation  , String> {


    List<Reservation> findByUserName(String username);

    long countByUserNameAndServiceNameAndIsConfirmed(String userName, String serviceName,String Isconfrmed);


}
