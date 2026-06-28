package com.azercell.marketplace.catalog.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to create a brand")
public record CreateBrandRequest(
        @Schema(description = "Display name (unique, case-insensitive)", example = "Apple", maxLength = 140)
        @NotBlank @Size(max = 140) String name,

        @Schema(description = "Short code, 2–20 chars of letters/digits; stored UPPERCASE and unique", example = "APPL")
        @NotBlank @Size(min = 2, max = 20) String code
) {
}
