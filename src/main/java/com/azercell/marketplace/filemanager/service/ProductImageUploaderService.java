package com.azercell.marketplace.filemanager.service;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.filemanager.storage.FileStorage;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductImageUploaderService {

    private final FileStorage fileStorage;

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "image/webp"
    );

    public ProductImageUploaderService(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
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

    /** For the local-disk serving endpoint; empty in S3 mode (images are served by S3/CDN directly). */
    public Optional<Resource> loadImage(String filename) {
        return fileStorage.loadAsResource(filename);
    }

    /**
     * Validates the MIME type, derives a unique object name, and hands the actual persistence to the
     * profile-selected {@link FileStorage} (disk locally, S3 in qa/prod). Returns the public URL.
     */
    private String uploadSingleImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new DomainException(ErrorCode.FILE_TYPE_UNSUPPORTED);
        }
        return fileStorage.store(file, uniqueObjectName(file, contentType));
    }

    private String uniqueObjectName(MultipartFile file, String contentType) {
        String originalFilename = file.getOriginalFilename();
        String extension;
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        } else {
            extension = contentType.equals("image/png") ? ".png" : ".jpg";
        }
        return UUID.randomUUID() + extension;
    }
}
