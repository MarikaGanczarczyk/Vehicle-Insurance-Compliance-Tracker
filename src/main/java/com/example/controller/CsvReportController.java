package com.example.controller;

import com.example.entity.VehicleViolation;
import com.example.service.CsvReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
public class CsvReportController {
   // Creates an API endpoint that lets users download a CSV file from the browser.
    private CsvReportService csvReportService;

    public CsvReportController(CsvReportService csvReportService) {
        this.csvReportService = csvReportService;
    }




    @GetMapping("/download/{date}")
    public ResponseEntity<byte[]> downloadCsv(@PathVariable String date) { // returns HTTP response and contains byte[] (file data)

        String csvData = csvReportService.generateCsv(date); // we call here our service ("get Csv report as String")

        byte[] bytes = csvData.getBytes(StandardCharsets.UTF_8); // we convert text into binary format to be able to downloaded , UTF_8 - Supports all characters and works with special letters



        String fileName =
                "vehicle_insurance_violations_" +
                        date +
                        ".csv";


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)// Content disposition tells the browser to download this file insted of displaying it
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(bytes);
    }




}
