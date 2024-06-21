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
        Optional<ContratAssurance> contratOptional = getContratByEmail(email);
        if (contratOptional.isPresent()) {
            ContratAssurance contrat = contratOptional.get();
            String nombreDeclarations = contrat.getNombreDeclarations();

            // Split the nombreDeclarations into parts
            String[] parts = nombreDeclarations.split("/");
            if (parts.length == 2 && parts[0].equals(parts[1])) {
                // If the declaration is of the form "x/x" where x is the same on both sides
                return Collections.emptyList();
            }

            String services = contrat.getServices();
            List<String> servicesList = Arrays.asList(services.split(", "));
            List<ServiceContrat> servicesObjects = serviceRepository.findByServiceNameIn(servicesList);

            // Check reservation counts for each service and remove if count is 3
            servicesObjects.removeIf(service -> {
                long reservationCount = reservationService.countReservations(contrat.getNomAssure(), service.getServiceName());
                if (reservationCount == 3) {
                    System.out.println("Service: " + service.getServiceName() + " est complet");
                    return true; // Remove the service
                }
                return false; // Keep the service
            });

            return servicesObjects;
        } else {
            return Collections.emptyList();
        }
    }



    public Optional<ServiceContrat> getServiceByName(String serviceName) {
        return Optional.ofNullable(serviceRepository.findByServiceName(serviceName));
    }




}