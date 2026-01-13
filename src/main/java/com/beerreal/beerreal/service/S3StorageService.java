package com.beerreal.beerreal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@Profile("prod")
public class S3StorageService implements StorageService {

    private final S3Client s3Client;

    @Value("${storage.s3.bucket}")
    private String bucket;

    @Value("${storage.s3.endpoint:}")
    private String endpoint;

    @Value("${storage.s3.region:us-east-1}")
    private String region;

    @Value("${storage.s3.public-url:}")
    private String publicUrl;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Cannot store empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String filename = UUID.randomUUID().toString() + extension;

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(filename)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()));

            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file in S3", e);
        }
    }

    @Override
    public String getFileUrl(String filename) {
        // If a public URL is configured (e.g., Cloudflare R2 public bucket URL)
        if (publicUrl != null && !publicUrl.isEmpty()) {
            return publicUrl + "/" + filename;
        }
        // Standard S3 URL
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, filename);
    }
}
