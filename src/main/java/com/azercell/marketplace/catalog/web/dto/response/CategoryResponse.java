package com.azercell.marketplace.catalog.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "A product category")
public record CategoryResponse(
        @Schema(description = "Category id") UUID id,
        @Schema(description = "Display name", example = "Phones") String name,
        @Schema(description = "URL slug (lowercase)", example = "phones") String slug,
        @Schema(description = "Optional description", nullable = true) String description,
        @Schema(description = "Lifecycle status", example = "ACTIVE", allowableValues = {"ACTIVE", "IN_ACTIVE"}) String status,
        @Schema(description = "Parent category id; null = root", nullable = true) UUID parentId
) {
}
