package com.azercell.marketplace.filemanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Files", description = "Upload and serve product variant images.")
public interface FileUploadApi {

    @Operation(summary = "Upload variant images",
            description = "Multipart form upload. Each form field name keys a variant; its file(s) are stored and the "
                    + "public URLs are returned, grouped by that field name. Allowed types: PNG, JPG, JPEG, WEBP.")
    ResponseEntity<Map<String, List<String>>> uploadProductVariantImages(
            MultiValueMap<String, MultipartFile> variantFilesMap);

    @Operation(summary = "Get a locally-stored image by file name",
            description = "Serves image bytes from local disk (dev/local profile). In qa/prod images live in S3 and "
                    + "the upload endpoint returns the S3/CDN URL directly, so this endpoint returns 404 there. "
                    + "Public: no token required; Content-Type is detected from the file.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Image bytes",
                    content = @Content(mediaType = "image/*",
                            schema = @Schema(type = "string", format = "binary"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No image with that file name",
                    content = @Content)
    })
    ResponseEntity<Resource> getImage(
            @Parameter(description = "Stored file name, e.g. 9f1c2b7a-….png") String filename);
}
