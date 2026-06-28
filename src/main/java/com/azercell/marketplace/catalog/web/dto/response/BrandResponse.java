package com.azercell.marketplace.catalog.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "A product brand")
public record BrandResponse(
        @Schema(description = "Brand id") UUID id,
        @Schema(description = "Display name", example = "Apple") String name,
        @Schema(description = "Short code (uppercase)", example = "APPL") String code,
        @Schema(description = "Lifecycle status", example = "ACTIVE", allowableValues = {"ACTIVE", "IN_ACTIVE"}) String status
) {
}
