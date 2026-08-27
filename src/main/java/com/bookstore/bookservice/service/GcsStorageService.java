package com.bookstore.bookservice.service;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class GcsStorageService {

    @Value("${gcp.storage.bucket.book-covers:its2130-book-covers}")
    private String bucketName;

    public String uploadCoverImage(MultipartFile file) throws IOException {
        String fileName = "cover_" + UUID.randomUUID().toString().substring(0, 8) + "_" + file.getOriginalFilename();
        try {
            Storage storage = StorageOptions.getDefaultInstance().getService();
            if (storage != null) {
                BlobId blobId = BlobId.of(bucketName, fileName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                        .setContentType(file.getContentType() != null ? file.getContentType() : "image/jpeg")
                        .build();
                storage.create(blobInfo, file.getBytes());
            }
        } catch (Exception e) {
            // If local or GCP credentials not set in dev, return simulated GCS public URL
            System.out.println("Notice: GCS bucket upload simulated for learning dev mode: " + e.getMessage());
        }
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }
}
