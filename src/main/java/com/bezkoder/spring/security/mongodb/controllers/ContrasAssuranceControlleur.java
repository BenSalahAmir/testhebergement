package com.bezkoder.spring.security.mongodb.controllers;


import com.bezkoder.spring.security.mongodb.Service.ContratAssuranceService;
import com.bezkoder.spring.security.mongodb.models.ContratAssurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/contrat")
@CrossOrigin(origins = "https://funny-fairy-17b429.netlify.app")
public class ContrasAssuranceControlleur {

    @Autowired
    private ContratAssuranceService contratAssuranceService;

    @PostMapping("/add")
    public ContratAssurance addContrat(@RequestBody ContratAssurance contratAssurance) {
        return contratAssuranceService.saveContratAssurance(contratAssurance);
    }

    @PutMapping("/update/{id}")
    public ContratAssurance updateContrat(@PathVariable String id, @RequestBody ContratAssurance updatedContrat) {
        return contratAssuranceService.updateContratAssurance(id, updatedContrat);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteContrat(@PathVariable String id) {
        contratAssuranceService.deleteContratAssurance(id);
    }

    @GetMapping("/all")
    public List<ContratAssurance> getAllContrats() {
        return contratAssuranceService.getAllContrats();
    }

    @GetMapping("/getbyid/{id}")
    public ContratAssurance getContratbyid(@PathVariable String id) {
        return contratAssuranceService.getcontratbyId(id);
    }

    @PostMapping("/save-from-excel")
    public void saveContratsFromExcel(@RequestParam("file") MultipartFile file) throws IOException {
        contratAssuranceService.saveContratsFromExcel(file);
    }
}