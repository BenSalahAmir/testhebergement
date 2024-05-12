package com.bezkoder.spring.security.mongodb.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.bezkoder.spring.security.mongodb.models.ContratAssurance;
import com.bezkoder.spring.security.mongodb.repository.ContratAssuranceRepository;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ContratAssuranceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContratAssuranceService.class);

    private final ContratAssuranceRepository contratAssuranceRepository;

    @Autowired
    public ContratAssuranceService(ContratAssuranceRepository contratAssuranceRepository) {
        this.contratAssuranceRepository = contratAssuranceRepository;
    }




    public void saveContratsFromExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);


            LOGGER.info("Starting to read and save data from Excel file...");

            // Iterate through each row starting from B6 (assuming data starts from B6)
            for (int i = 5; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    LOGGER.warn("Row is null at index {}", i);
                    continue; // Skip to next iteration
                }

                // Create ContratAssurance object for each row
                ContratAssurance contratAssurance = new ContratAssurance();

                // Read and populate attributes of ContratAssurance object from cells
                Cell compagnieCell = row.getCell(1); // Compagnie d'assurance
                Cell typeAssuranceCell = row.getCell(2); // Type de l'assurance
                Cell nomAssureCell = row.getCell(3); // Nom de l'assuré
                Cell numeroSouscriptionCell = row.getCell(4); // Numéro de souscription
                Cell telephoneCell = row.getCell(5); // Téléphone (Column K)
                Cell adresseMailCell = row.getCell(6); // Adresse mail (Column L)
                Cell beneficiaire1Cell = row.getCell(7); // Bénéficiaire 1
                Cell regionCell = row.getCell(8); // Région
                Cell servicesCell = row.getCell(9); // Services
                Cell plafondCell = row.getCell(10); // Plafond
                Cell nombreDeclarationsCell = row.getCell(11); // Nombre de déclaration
                Cell exclusionCell = row.getCell(12); // Exclusion (Column M)


                // Check if any cell is null or empty
                if (compagnieCell == null || compagnieCell.getCellType() != CellType.STRING || compagnieCell.getStringCellValue().isEmpty() ||
                        typeAssuranceCell == null || typeAssuranceCell.getCellType() != CellType.STRING || typeAssuranceCell.getStringCellValue().isEmpty() ||
                        nomAssureCell == null || nomAssureCell.getCellType() != CellType.STRING || nomAssureCell.getStringCellValue().isEmpty() ||
                        beneficiaire1Cell == null || beneficiaire1Cell.getCellType() != CellType.STRING || beneficiaire1Cell.getStringCellValue().isEmpty() ||
                        regionCell == null || regionCell.getCellType() != CellType.STRING || regionCell.getStringCellValue().isEmpty() ||
                        servicesCell == null || servicesCell.getCellType() != CellType.STRING || servicesCell.getStringCellValue().isEmpty() ||
                        plafondCell == null || plafondCell.getCellType() != CellType.STRING || plafondCell.getStringCellValue().isEmpty() ||
                        exclusionCell == null || exclusionCell.getCellType() != CellType.STRING || exclusionCell.getStringCellValue().isEmpty() ||
                        adresseMailCell == null || adresseMailCell.getCellType() != CellType.STRING || adresseMailCell.getStringCellValue().isEmpty() ||
                        nombreDeclarationsCell == null || nombreDeclarationsCell.getCellType() != CellType.STRING || nombreDeclarationsCell.getStringCellValue().isEmpty()) {


                    // Skip this row if any cell is null or empty
                    LOGGER.warn("Skipping empty or incomplete contract data for row {}", i + 1);
                    continue; // Skip to next row
                }

                // Populate ContratAssurance object with data
                contratAssurance.setCompagnieAssurance(compagnieCell.getStringCellValue());
                contratAssurance.setTypeAssurance(typeAssuranceCell.getStringCellValue());
                contratAssurance.setNomAssure(nomAssureCell.getStringCellValue());
                contratAssurance.setBeneficiaire1(beneficiaire1Cell.getStringCellValue());
                contratAssurance.setRegion(regionCell.getStringCellValue());
                contratAssurance.setServices(servicesCell.getStringCellValue());
                contratAssurance.setPlafond(plafondCell.getStringCellValue());
                contratAssurance.setNombreDeclarations(nombreDeclarationsCell.getStringCellValue());
                contratAssurance.setAdressemail(adresseMailCell.getStringCellValue());
                contratAssurance.setExclusion(exclusionCell.getStringCellValue());


                // Handle Numéro de souscription cell
                if (numeroSouscriptionCell != null) {
                    if (numeroSouscriptionCell.getCellType() == CellType.NUMERIC) {
                        double numeroSouscriptionValue = numeroSouscriptionCell.getNumericCellValue();
                        contratAssurance.setNumeroSouscription((int) numeroSouscriptionValue);
                    } else if (numeroSouscriptionCell.getCellType() == CellType.STRING) {
                        String numeroSouscriptionCellValue = numeroSouscriptionCell.getStringCellValue();
                        if (!numeroSouscriptionCellValue.isEmpty()) {
                            try {
                                int numeroSouscriptionValue = Integer.parseInt(numeroSouscriptionCellValue);
                                contratAssurance.setNumeroSouscription(numeroSouscriptionValue);
                            } catch (NumberFormatException e) {
                                LOGGER.warn("Invalid number format for Numéro de souscription at row {}: {}", i + 1, numeroSouscriptionCellValue);
                            }
                        } else {
                            LOGGER.warn("Empty value for Numéro de souscription at row {}", i + 1);
                        }
                    } else {
                        LOGGER.warn("Unsupported cell type for Numéro de souscription at row {}: {}", i + 1, numeroSouscriptionCell.getCellType());
                    }
                } else {
                    LOGGER.warn("Numéro de souscription cell is null at row {}", i + 1);
                }







                if (telephoneCell != null) {
                    if (telephoneCell.getCellType() == CellType.NUMERIC) {
                        double telephoneValue = telephoneCell.getNumericCellValue();
                        contratAssurance.setTelephone((int) telephoneValue);
                    } else if (telephoneCell.getCellType() == CellType.STRING) {
                        String telephoneCellValue = telephoneCell.getStringCellValue();
                        if (!telephoneCellValue.isEmpty()) {
                            try {
                                int numeroSouscriptionValue = Integer.parseInt(telephoneCellValue);
                                contratAssurance.setTelephone(numeroSouscriptionValue);
                            } catch (NumberFormatException e) {
                                LOGGER.warn("Invalid number format for Telephone  at row {}: {}", i + 1, telephoneCellValue);
                            }
                        } else {
                            LOGGER.warn("Empty value for Telephone at row {}", i + 1);
                        }
                    } else {
                        LOGGER.warn("Unsupported cell type for Telephone at row {}: {}", i + 1, numeroSouscriptionCell.getCellType());
                    }
                } else {
                    LOGGER.warn("Telephone cell is null at row {}", i + 1);
                }





                // Save the contract to MongoDB using the repository
                contratAssuranceRepository.save(contratAssurance);
                LOGGER.info("Saved data for row: {}", i + 1);
            }

            LOGGER.info("Data from Excel file saved successfully.");
        } catch (IOException e) {
            LOGGER.error("Error occurred while reading or saving data from Excel file:", e);
        }
    }



    public ContratAssurance saveContratAssurance(ContratAssurance contratAssurance) {
        return contratAssuranceRepository.save(contratAssurance);
    }

    public ContratAssurance updateContratAssurance(String id, ContratAssurance updatedContratAssurance) {
        Optional<ContratAssurance> existingContrat = contratAssuranceRepository.findById(id);
        if (existingContrat.isPresent()) {
            ContratAssurance contratToUpdate = existingContrat.get();
            contratToUpdate.setCompagnieAssurance(updatedContratAssurance.getCompagnieAssurance());
            contratToUpdate.setTypeAssurance(updatedContratAssurance.getTypeAssurance());
            contratToUpdate.setNomAssure(updatedContratAssurance.getNomAssure());
            contratToUpdate.setNumeroSouscription(updatedContratAssurance.getNumeroSouscription());
            contratToUpdate.setBeneficiaire1(updatedContratAssurance.getBeneficiaire1());
            contratToUpdate.setRegion(updatedContratAssurance.getRegion());
            contratToUpdate.setServices(updatedContratAssurance.getServices());
            contratToUpdate.setPlafond(updatedContratAssurance.getPlafond());
            contratToUpdate.setNombreDeclarations(updatedContratAssurance.getNombreDeclarations());

            return contratAssuranceRepository.save(contratToUpdate);
        } else {
            return null;
        }
    }

    public void deleteContratAssurance(String id) {
        contratAssuranceRepository.deleteById(id);
    }

    public List<ContratAssurance> getAllContrats() {
        return contratAssuranceRepository.findAll();
    }

    public ContratAssurance getcontratbyId(String id){
        return contratAssuranceRepository.findById(id).orElse(null);
    }
}