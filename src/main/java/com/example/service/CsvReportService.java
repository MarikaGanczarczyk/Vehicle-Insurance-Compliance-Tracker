package com.example.service;

import com.example.entity.VehicleViolation;
import com.example.repository.VehicleViolationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CsvReportService {
    // Reads violations from the database and converts them into CSV text format.

    private  VehicleViolationRepository vehicleViolationRepository;

    public CsvReportService(VehicleViolationRepository vehicleViolationRepository) {
        this.vehicleViolationRepository = vehicleViolationRepository;
    }


    public String generateCsv(String date) { //String because it will return text

        LocalDate parsedDate = LocalDate.parse(date); //"2025-05-27"
       //List<VehicleViolation> violations = vehicleViolationRepository.findAll(); // we get all violation records from db

        List<VehicleViolation> violations = vehicleViolationRepository
                .findByDate(parsedDate);

        StringBuilder csv = new StringBuilder(); // StringBuilder is a build-in class from Java
        csv.append("vehicleId,violationType,dateTime,description\n"); // append is a method from StringBuilder to add a header


        //  Write every violation as one line
        for (VehicleViolation v : violations) {
            csv.append(v.getVehicleId()).append(",");
            csv.append(v.getViolationType()).append(",");
            csv.append(v.getDateTime()).append(",");
            csv.append(v.getDescription()).append("\n");
        }

        return csv.toString();
    }


}
