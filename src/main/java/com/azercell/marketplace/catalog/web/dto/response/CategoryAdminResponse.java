package com.azercell.marketplace.catalog.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "A category with both languages (admin view)")
public record CategoryAdminResponse(
        @Schema(description = "Category id") UUID id,
        @Schema(description = "Display name (Azerbaijani)", example = "Telefonlar") String nameAz,
        @Schema(description = "Display name (English)", example = "Phones") String nameEn,
        @Schema(description = "URL slug (lowercase, language-neutral)", example = "phones") String slug,
        @Schema(description = "Description (Azerbaijani)", nullable = true) String descriptionAz,
        @Schema(description = "Description (English)", nullable = true) String descriptionEn,
        @Schema(description = "Lifecycle status", example = "ACTIVE", allowableValues = {"ACTIVE", "IN_ACTIVE"}) String status,
        @Schema(description = "Parent category id; null = root", nullable = true) UUID parentId
) {
}
