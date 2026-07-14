package com.azercell.marketplace.filemanager.controller;

import com.azercell.marketplace.filemanager.service.ProductImageUploaderService;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
public class ProductImageUploaderController implements FileUploadApi {

    private final ProductImageUploaderService uploaderService;

    public ProductImageUploaderController(ProductImageUploaderService uploaderService) {
        this.uploaderService = uploaderService;
    }

    @PostMapping(value = "/api/v1/files/upload-variant-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, List<String>>> uploadProductVariantImages(
            @RequestParam MultiValueMap<String, MultipartFile> variantFilesMap) {
        Map<String, List<String>> response = uploaderService.processVariantImagesUpload(variantFilesMap);

        return ResponseEntity.ok(response);
    }

    /**
     * Serves a locally-stored image by file name (local profile). In S3 mode the returned upload URL points
     * straight at S3/CDN, so this returns 404 — nothing is served from disk.
     */
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        return uploaderService.loadImage(filename)
                .map(resource -> ResponseEntity.ok()
                        .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .cacheControl(CacheControl.maxAge(Duration.ofDays(30)).cachePublic())
                        .body(resource))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
