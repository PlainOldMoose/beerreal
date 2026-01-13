package com.beerreal.beerreal.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    String storeFile(MultipartFile file);
    String getFileUrl(String filename);
}
