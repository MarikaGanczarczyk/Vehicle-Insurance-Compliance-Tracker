package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Service
public class AwsS3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public AwsS3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(File file) {
        System.out.println(">>> Uploading file: " + file.getName());
        System.out.println(">>> File exists: " + file.exists());
        System.out.println(">>> Bucket: " + bucketName);
        System.out.println(">>> Region: " + file.getAbsolutePath());
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getName())        // nazwa pliku w S3
                .contentType("text/csv")
                .build();

        s3Client.putObject(request, RequestBody.fromFile(file));
        System.out.println(">>> Upload successful!");
        return "https://" + bucketName + ".s3.amazonaws.com/" + file.getName();
    }
}
