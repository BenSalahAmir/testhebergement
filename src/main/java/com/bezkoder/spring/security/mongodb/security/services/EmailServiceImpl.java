package com.bezkoder.spring.security.mongodb.security.services;

import com.bezkoder.spring.security.mongodb.models.User;
import com.bezkoder.spring.security.mongodb.models.UserMail;
import com.bezkoder.spring.security.mongodb.repository.IUserEmailRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@Slf4j
public class EmailServiceImpl implements IUserEmailRepository {
/*
    @Value("${app.TWILIO_AUTH_TOKEN}")
    private  String Service_TWILIO_AUTH_TOKEN;
    @Value("${app.WILIO_ACCOUNT_SID}")
    private  String Service_TWILIO_ACCOUNT_SID;
    */


    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public void sendCodeByMail(UserMail mail) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("saebizmatch@gmail.com");
            helper.setTo(mail.getTo());
            helper.setSubject("Code d'activation");

            String htmlMsg = "<div style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 50px;'>" +
                    "<div style='background-color: #ffffff; padding: 40px; border-radius: 8px; max-width: 600px; margin: auto;'>" +
                    "<h1 style='color: #333333; font-size: 24px; margin-bottom: 20px;'>Bienvenue chez Elaa International !</h1>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 20px;'>Merci de vous être inscrit. Veuillez utiliser le code suivant pour compléter votre inscription :</p>" +
                    "<p style='background-color: #48cfae; display: inline-block; padding: 10px 20px; color: #ffffff; border-radius: 4px; font-size: 18px; font-weight: bold;'>" + mail.getCode() + "</p>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-top: 20px;'>Si vous n'avez pas effectué cette demande, vous pouvez ignorer cet e-mail.</p>" +
                    "</div></div>";

            message.setContent(htmlMsg, "text/html");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendReservationConfirmationMail(String serviceClientEmail, String userName, String serviceName, LocalDateTime reservationDateTime) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("saebizmatch@gmail.com");
            helper.setTo(serviceClientEmail);
            helper.setSubject("une demande de reservation");

            // Format reservation date time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedReservationDateTime = reservationDateTime.format(formatter);

            // Update the email body to inform the service client about the new reservation
            String htmlMsg = "<div style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 50px;'>" +
                    "<div style='background-color: #ffffff; padding: 40px; border-radius: 8px; max-width: 600px; margin: auto;'>" +
                    "<h1 style='color: #333333; font-size: 24px; margin-bottom: 20px;'>Confirmation de reservation</h1>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 20px;'>Une nouvelle reservation a ete effectuee par <strong>" + userName + "</strong> pour le service <strong>" + serviceName + "</strong>.</p>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 20px;'>Date et heure de la reservation : <strong>" + formattedReservationDateTime + "</strong></p>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-top: 20px;'>Veuillez verifier la plateforme pour confirmer la reservation.</p>" +
                    "</div></div>";

            message.setContent(htmlMsg, "text/html");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendconfirmationtouser(String serviceClientEmail, String userName, String serviceName, LocalDateTime reservationDateTime) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("saebizmatch@gmail.com");
            helper.setTo(serviceClientEmail);
            helper.setSubject("Confirmation de réservation");

            // Format reservation date time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedReservationDateTime = reservationDateTime.format(formatter);

            // Email body to inform the client that the reservation is confirmed
            String htmlMsg = "<div style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 50px;'>" +
                    "<div style='background-color: #ffffff; padding: 40px; border-radius: 8px; max-width: 600px; margin: auto;'>" +
                    "<h1 style='color: #333333; font-size: 24px; margin-bottom: 20px;'>Confirmation de réservation</h1>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 20px;'>Bonjour <strong>" + userName + "</strong>,</p>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 20px;'>Votre réservation pour le service <strong>" + serviceName + "</strong> a été confirmée avec succès.</p>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 20px;'>Votre réservation pour le service <strong>" + serviceName + "</strong> a été confirmée avec succès.</p>" +

                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 20px;'>Date et heure de la réservation : <strong>" + formattedReservationDateTime + "</strong></p>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-top: 20px;'>Merci pour votre confiance .</p>" +
                    "</div></div>";

            message.setContent(htmlMsg, "text/html");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendcodereset(UserMail mail) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("saebizmatch@gmail.com");
            helper.setTo(mail.getTo());
            helper.setSubject("Code de reinitialisation du mot de passe");

            String htmlMsg = "<div style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 50px;'>" +
                    "<div style='background-color: #ffffff; padding: 40px; border-radius: 8px; max-width: 600px; margin: auto;'>" +
                    "<h1 style='color: #333333; font-size: 24px; margin-bottom: 20px;'>Demande de reinitialisation du mot de passe</h1>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 20px;'>Vous avez demandé à reinitialiser votre mot de passe. Veuillez utiliser le code suivant pour reinitialiser votre mot de passe:</p>" +
                    "<p style='background-color: #48cfae; display: inline-block; padding: 10px 20px; color: #ffffff; border-radius: 4px; font-size: 18px; font-weight: bold;'>" + mail.getCode() + "</p>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-top: 20px;'>Si vous n'avez pas effectue cette demande, veuillez nous contacter immediatement.</p>" +
                    "</div></div>";

            message.setContent(htmlMsg, "text/html");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }




    public void sendVerificationEmail(User user) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            user.setVerificationToken(verificationTokenService.generateVerificationToken());
            helper.setSubject("Vérification du compte");

            String htmlMsg = "<div style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 50px;'>" +
                    "<div style='background-color: #ffffff; padding: 40px; border-radius: 8px; max-width: 600px; margin: auto;'>" +
                    "<h1 style='color: #333333; font-size: 24px; margin-bottom: 20px;'>Bonjour " + user.getUsername() + ",</h1>" +
                    "<p style='color: #666666; font-size: 16px; line-height: 1.5; margin-bottom: 20px;'>Veuillez cliquer sur le lien ci-dessous pour activer votre compte :</p>" +
                    "<a href='https://testhebergement.onrender.com/api/auth/activate?token=" + user.getVerificationToken() + "' style='background-color: #48cfae; display: inline-block; padding: 10px 20px; color: #ffffff; border-radius: 4px; font-size: 18px; font-weight: bold; text-decoration: none;'>Activer le compte</a>" +
                    "</div></div>";

            message.setContent(htmlMsg, "text/html");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }




    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

/*
    @Async
    public String SendSms(String Phone, String message){
        Twilio.init(Service_TWILIO_ACCOUNT_SID, Service_TWILIO_AUTH_TOKEN);
        Message.creator(new PhoneNumber(Phone),
                new PhoneNumber("+12764449446"), message).create();
        log.info("Sms Send");
        return "Message sent successfully";
    }
    */


}
