package com.bezkoder.spring.security.mongodb.controllers;


import com.bezkoder.spring.security.mongodb.models.Reservation;
import com.bezkoder.spring.security.mongodb.repository.ContratAssuranceRepository;
import com.bezkoder.spring.security.mongodb.security.services.ReservationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;


@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")

public class ReservationController {

    @Autowired
    private ReservationServiceImp reservationService;

    @Autowired
    private ContratAssuranceRepository contratAssuranceRepository;






    @GetMapping("/getbyusername/{username}")
    public List<Reservation> getReservationsByUsername(@PathVariable String username) {
        return reservationService.getReservationsByUsername(username);
    }

    @GetMapping("/getall")
    public List<Reservation> getAllReservations() {
        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbb"+reservationService.getAllReservations());
        return reservationService.getAllReservations();
    }

    @GetMapping("/getbyid/{id}")
    public Reservation getReservationById(@PathVariable String id) {
        return reservationService.getReservationById(id);
    }

    @PostMapping("/add")
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<Reservation> confirmReservation(@PathVariable String id, @RequestBody String userConfirmation) {
        try {
            Reservation confirmedReservation = reservationService.confirmReservation(id, userConfirmation);



            return new ResponseEntity<>(confirmedReservation, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/finMission/{id}")
    public ResponseEntity<Reservation> FinMissionReservation(@PathVariable String id) {
        try {
            Reservation FinMissionReservation = reservationService.finMissionReservation(id);
            return new ResponseEntity<>(FinMissionReservation, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/nombre-declarations")
    public ResponseEntity<String> getNombreDeclarations(@RequestParam String email) {
        String result = reservationService.getnombredeclaration(email);
        if ("email not found".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



    @GetMapping("/count/{userName}/{serviceName}")
    public ResponseEntity<Long> countReservations(
            @PathVariable String userName,
            @PathVariable String serviceName) {
        long count = reservationService.countReservations(userName, serviceName);
        return ResponseEntity.ok(count);
    }


    @GetMapping("/gat")
    public List<Reservation> getReservationsForGATContracts() {
        return reservationService.getReservationsForGATContracts();
    }




}