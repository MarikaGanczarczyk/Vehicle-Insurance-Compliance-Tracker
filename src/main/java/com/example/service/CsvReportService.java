package com.example.service;

import com.example.entity.VehicleViolation;
import com.example.repository.VehicleViolationRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

//First, the CSV data is generated as a String. Then, it is written into a physical file using File and FileWriter. This file can then be used for further processing, such as uploading to AWS S3.

@Service
public class CsvReportService {
    // Reads violations from the database and converts them into CSV text format.

    private  VehicleViolationRepository vehicleViolationRepository;
    private final AwsS3Service awsS3Service;
    public final SqsService sqsService;
    public CsvReportService(VehicleViolationRepository vehicleViolationRepository, AwsS3Service awsS3Service, SqsService sqsService) {
        this.vehicleViolationRepository = vehicleViolationRepository;
        this.awsS3Service = awsS3Service;
        this.sqsService = sqsService;
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


// This converts String report into csv file
    public File generateCsvFile(String date) throws IOException {// File is a Class in java
        String csvData = generateCsv(date);

        String fileName = "violations_" + date + ".csv";
        File file = new File(fileName);

        try (FileWriter writer = new FileWriter(file)) { // This Class creates the file on the disk
            writer.write(csvData);
        }

        return file;
    }

//    public String generateAndUploadCsv(String date) throws IOException {
//        File file = generateCsvFile(date);       // creates file
//        String url = awsS3Service.uploadFile(file); // sends to the aws
//        file.delete();                           //removes local file
//        return url;
//    }

    public File generateCsvAndUpload(String date) throws IOException {

        File file = generateCsvFile(date);// creates file
        System.out.println(" CSV generated: " + file.getName());


        String url = awsS3Service.uploadFile(file); // sends to the aws
        System.out.println(" Uploaded to S3: " + url);


        String message = String.format ( "CSV Report Ready | Date: %s  | File: %s",
        date, url);

        sqsService.sendMessage(message);
        System.out.println(" Message sent to SQS" + message);

        return file;
    }


}
