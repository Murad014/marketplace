package com.azercell.marketplace.filemanager.service;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.*;

@Service
public class ProductImageUploaderService {

    private final Path storageDirectory = Paths.get("uploads/images");

    @Value("${app.base-url:}")
    private String baseUrl;

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "image/webp"
    );

    public ProductImageUploaderService() {
        try {
            Files.createDirectories(storageDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory path", e);
        }
    }

    /**
     * Loads a stored image by file name for serving. Guards against path traversal (the resolved path must
     * stay inside the storage directory) and returns 404 when the file is missing, so a broken image URL is
     * a clean not-found rather than an error.
     */
    public ResponseEntity<Resource> loadImageAsResource(String filename) {
        Path base = storageDirectory.toAbsolutePath().normalize();
        Path target = base.resolve(filename).normalize();
        if (!target.startsWith(base)) {
            return ResponseEntity.notFound().build();   // path traversal attempt (e.g. ../../etc)
        }

        try {
            Resource resource = new UrlResource(target.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(target);
            if (contentType == null) contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .cacheControl(CacheControl.maxAge(Duration.ofDays(30)).cachePublic())
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file.", e);
        }
    }

    public Map<String, List<String>> processVariantImagesUpload(MultiValueMap<String, MultipartFile> variantFilesMap) {
        Map<String, List<String>> structureResponseMap = new HashMap<>();

        for (Map.Entry<String, List<MultipartFile>> entry : variantFilesMap.entrySet()) {
            String variantKeyName = entry.getKey();
            List<MultipartFile> structuralFiles = entry.getValue();
            List<String> successfullyUploadedUrls = new ArrayList<>();

            for (MultipartFile file : structuralFiles) {
                if (!file.isEmpty()) {
                    String cleanUrl = uploadSingleImage(file);
                    successfullyUploadedUrls.add(cleanUrl);
                }
            }

            structureResponseMap.put(variantKeyName, successfullyUploadedUrls);
        }

        return structureResponseMap;
    }

    /**
     * Low-level helper method handling specific file validation and disk storage
     */
    private String uploadSingleImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new DomainException(ErrorCode.FILE_TYPE_UNSUPPORTED);
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } else {
                extension = contentType.equals("image/png") ? ".png" : ".jpg";
            }

            String uniqueFilename = UUID.randomUUID().toString() + extension;
            Path targetLocation = storageDirectory.resolve(uniqueFilename);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/images/" + uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("IO Error tracking: Failed to store image file.", e);
        }
    }
}