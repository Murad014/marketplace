package com.azercell.marketplace.catalog.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to update a brand. All fields are applied; `active` is optional.")
public record UpdateBrandRequest(
        @Schema(description = "Display name (unique, case-insensitive)", example = "Apple Computer", maxLength = 140)
        @NotBlank @Size(max = 140) String name,

        @Schema(description = "Short code, 2–20 chars of letters/digits; stored UPPERCASE and unique", example = "APPL")
        @NotBlank @Size(min = 2, max = 20) String code,

        @Schema(description = "Active flag; null leaves the current status unchanged", example = "false", nullable = true)
        Boolean active
) {
}
