package com.azercell.marketplace.filemanager.controller;

import com.azercell.marketplace.filemanager.service.ProductImageUploaderService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /** Serves an uploaded image by file name (the URL returned by upload). Public storefront asset. */
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        return uploaderService.loadImageAsResource(filename);
    }
}
