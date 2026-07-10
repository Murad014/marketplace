package com.azercell.marketplace.catalog.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "A category localized to the request's Accept-Language (public view)")
public record CategoryResponse(
        @Schema(description = "Category id") UUID id,
        @Schema(description = "Display name in the requested language", example = "Phones") String name,
        @Schema(description = "URL slug (lowercase)", example = "phones") String slug,
        @Schema(description = "Description in the requested language", nullable = true) String description,
        @Schema(description = "Parent category id; null = root", nullable = true) UUID parentId
) {
}
