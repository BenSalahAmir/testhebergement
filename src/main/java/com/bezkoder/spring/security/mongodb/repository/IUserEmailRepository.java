package com.bezkoder.spring.security.mongodb.repository;

import com.bezkoder.spring.security.mongodb.models.UserMail;

import java.time.LocalDateTime;

public interface IUserEmailRepository {

    public void sendCodeByMail(UserMail mail);
    public void sendcodereset(UserMail mail);
    public void sendReservationConfirmationMail(String serviceClientEmail, String userName, String serviceName, LocalDateTime reservationDateTime) ;
    public void sendconfirmationtouser(String serviceClientEmail, String userName, String serviceName, LocalDateTime reservationDateTime) ;


}
