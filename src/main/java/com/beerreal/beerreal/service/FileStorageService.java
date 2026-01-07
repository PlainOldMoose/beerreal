package com.beerreal.beerreal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path uploadDir;

    public FileStorageService() {
        this.uploadDir = Paths.get(
                "uploads");
        try {
            Files.createDirectories(
                    this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create directory: " + this.uploadDir,
                    e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException(
                    "Could not store empty file");
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(
                ".")
                ? originalFilename.substring(
                originalFilename.lastIndexOf(
                        ".")) : "";
        String filename = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = this.uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation,
                    StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Path loadFile(String filename) {
        return uploadDir.resolve(filename);
    }
}
