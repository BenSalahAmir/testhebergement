package com.bezkoder.spring.security.mongodb.repository;

import com.bezkoder.spring.security.mongodb.models.ContratAssurance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContratAssuranceRepository extends MongoRepository<ContratAssurance, String> {

    boolean existsByNumeroSouscription(int numeroSouscription);
    Boolean existsByAdressemail(String AdressMail);

    ContratAssurance getContratAssurancesByAdressemail(String AdressMail);

    Optional<ContratAssurance> findByAdressemail(String adresseMail);


}
