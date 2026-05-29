package com.example.service;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SqsService {


    private final SqsTemplate sqsTemplate;

    public SqsService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void sendMessage(String message) {
        sqsTemplate.send("https://sqs.eu-west-1.amazonaws.com/126350205580/vehicle-report-queue", message);

        System.out.println(" Sent to SQS: " + message);
    }

}
