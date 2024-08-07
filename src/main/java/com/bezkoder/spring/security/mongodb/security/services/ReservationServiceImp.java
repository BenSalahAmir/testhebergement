package com.bezkoder.spring.security.mongodb.security.services;

import com.bezkoder.spring.security.mongodb.models.ContratAssurance;
import com.bezkoder.spring.security.mongodb.models.Reservation;
import com.bezkoder.spring.security.mongodb.models.User;
import com.bezkoder.spring.security.mongodb.repository.ContratAssuranceRepository;
import com.bezkoder.spring.security.mongodb.repository.ReservationRepository;
import com.bezkoder.spring.security.mongodb.repository.UserRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReservationServiceImp {



    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContratAssuranceRepository contratAssuranceRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    Logger logger = LoggerFactory.getLogger(getClass());


    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(String id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    public Reservation createReservation(Reservation reservation) {
        reservation.setReservationDateTime(LocalDateTime.now());
        reservation.setIsConfirmed("Not Confirmed");
        reservation.setFinMission("Not Done");

        Optional<User> user = userRepository.findByUsername(reservation.getUserName());

        if (user.isPresent()) {
            //emailService.sendReservationConfirmationMail("marketing@elaa-international.com", reservation.getUserName(), reservation.getServiceName(), LocalDateTime.now());
            logger.warn("send mail success");

        } else {
            logger.warn("User not found for username: {}", reservation.getUserName());
        }

        return reservationRepository.save(reservation);
    }


    public List<Reservation> getReservationsByUsername(String username) {
        return reservationRepository.findByUserName(username);
    }






    public Reservation confirmReservation(String id, String userConfirmation) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        Optional<User> optionalUser = userRepository.findByUsername(optionalReservation.get().getUserName());



        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setIsConfirmed("Confirmed");
            reservation.setUserConfirmation(userConfirmation);

            sendConfirmationNotification(reservation.getUserName(), "Votre réservation pour le service " + reservation.getServiceName() + " a été confirmée");
            reservationRepository.save(reservation);

            Optional<User> user = userRepository.findByUsername(reservation.getUserName());
            Optional<ContratAssurance> contratAssuranceOptional = contratAssuranceRepository.findByAdressemail(user.get().getEmail());

            if (contratAssuranceOptional.isPresent()) {
                ContratAssurance contratAssurance = contratAssuranceOptional.get();
                String nombreDeclarations = contratAssurance.getNombreDeclarations();

                // Split the nombreDeclarations string to get the current count and total
                String[] parts = nombreDeclarations.split("/");
                int currentCount = Integer.parseInt(parts[0]);
                int totalCount = Integer.parseInt(parts[1]);

                if (currentCount < totalCount) {
                    currentCount++; // Increment current count
                    contratAssurance.setNombreDeclarations(currentCount + "/" + totalCount);
                    //emailService.sendconfirmationtouser(optionalUser.get().getEmail(),reservation.getUserName(),reservation.getServiceName(),LocalDateTime.now());
                    contratAssuranceRepository.save(contratAssurance);
                }
            }

            return reservation;
        } else {
            throw new RuntimeException("Reservation not found with id: " + id);
        }
    }

    public String getnombredeclaration(String email){
        ContratAssurance contratAssurance = contratAssuranceRepository.getContratAssurancesByAdressemail(email);
        if (contratAssurance!=null){
            return contratAssurance.getNombreDeclarations();
        }
        return "email not found";
    }


    private void sendConfirmationNotification(String username, String message) {
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
    }




    public long countReservations(String userName, String serviceName) {
        return reservationRepository.countByUserNameAndServiceNameAndIsConfirmed(userName, serviceName,"Confirmed");
    }



    public Reservation finMissionReservation (String id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setFinMission("Done");
            reservationRepository.save(reservation);
            return reservation;
        } else {
            throw new RuntimeException("Reservation not found with id: " + id);
        }
    }


    public List<Reservation> getReservationsForGATContracts() {
        // Fetch contracts where compagnieAssurance is GAT
        List<ContratAssurance> gatContracts = contratAssuranceRepository.findByCompagnieAssurance("GAT");

        // Extract usernames from these contracts
        List<String> userNames = gatContracts.stream()
                .map(ContratAssurance::getNomAssure)
                .collect(Collectors.toList());

        // Fetch reservations with these usernames
        return reservationRepository.findByUserNameIn(userNames);
    }








}

