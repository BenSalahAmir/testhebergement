package com.bezkoder.spring.security.mongodb.Service;

import com.bezkoder.spring.security.mongodb.models.ContratAssurance;
import com.bezkoder.spring.security.mongodb.models.ServiceContrat;
import com.bezkoder.spring.security.mongodb.repository.ContratAssuranceRepository;
import com.bezkoder.spring.security.mongodb.repository.ServiceRepository;
import com.bezkoder.spring.security.mongodb.security.services.ReservationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ServiceContratServiceImpl {

    @Autowired
    private ContratAssuranceRepository contratAssuranceRepository;


    private final ServiceRepository serviceRepository;

    @Autowired
    private ReservationServiceImp reservationService;

    @Autowired
    public ServiceContratServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }



    // Create
    public ServiceContrat createService(ServiceContrat service) {
        return serviceRepository.save(service);
    }

    // Read
    public List<ServiceContrat> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<ServiceContrat> getServiceById(String id) {
        return serviceRepository.findById(id);
    }

    // Update
    public ServiceContrat updateService(String id, ServiceContrat service) {
        service.setServiceId(id);
        return serviceRepository.save(service);
    }

    // Delete
    public void deleteService(String id) {
        serviceRepository.deleteById(id);
    }




    public Optional<ContratAssurance> getContratByEmail(String email) {
        return contratAssuranceRepository.findByAdressemail(email);
    }

    public List<ServiceContrat> getServicesByEmailAsObjects(String email) {
        return getContratByEmail(email)
                .map(this::processContrat)
                .orElse(Collections.emptyList());
    }

    private List<ServiceContrat> processContrat(ContratAssurance contrat) {
        if (isDeclarationsComplete(contrat.getNombreDeclarations())) {
            return Collections.emptyList();
        }

        List<String> servicesList = parseServices(contrat.getServices());
        List<ServiceContrat> servicesObjects = serviceRepository.findByServiceNameIn(servicesList);
        return filterCompleteServices(servicesObjects, contrat.getNomAssure());
    }

    private boolean isDeclarationsComplete(String nombreDeclarations) {
        String[] parts = nombreDeclarations.split("/");
        return parts.length == 2 && parts[0].equals(parts[1]);
    }

    private List<String> parseServices(String services) {
        return Arrays.asList(services.split(", "));
    }

    private List<ServiceContrat> filterCompleteServices(List<ServiceContrat> servicesObjects, String nomAssure) {
        return servicesObjects.stream()
                .filter(service -> !isServiceComplete(nomAssure, service.getServiceName()))
                .collect(Collectors.toList());
    }

    private boolean isServiceComplete(String nomAssure, String serviceName) {
        long reservationCount = reservationService.countReservations(nomAssure, serviceName);
        if (reservationCount == 3) {
            System.out.println("Service: " + serviceName + " est complet");
            return true;
        }
        return false;
    }




    public Optional<ServiceContrat> getServiceByName(String serviceName) {
        return Optional.ofNullable(serviceRepository.findByServiceName(serviceName));
    }




}