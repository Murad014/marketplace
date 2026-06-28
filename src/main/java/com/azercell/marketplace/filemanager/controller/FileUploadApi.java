package com.azercell.marketplace.filemanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Files", description = "Multipart upload of product variant images.")
public interface FileUploadApi {

    @Operation(summary = "Upload variant images",
            description = "Multipart form upload. Each form field name keys a variant; its file(s) are stored and the "
                    + "public URLs are returned, grouped by that field name. Allowed types: PNG, JPG, JPEG, WEBP.")
    ResponseEntity<Map<String, List<String>>> uploadProductVariantImages(
            MultiValueMap<String, MultipartFile> variantFilesMap);
}
