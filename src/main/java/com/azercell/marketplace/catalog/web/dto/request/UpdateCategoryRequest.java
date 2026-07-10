package com.azercell.marketplace.catalog.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Full category update. parentId null = make root; active is optional.")
public record UpdateCategoryRequest(
        @Schema(description = "Display name (Azerbaijani)", example = "Mobil telefonlar", maxLength = 120)
        @NotBlank @Size(max = 120) String nameAz,

        @Schema(description = "Display name (English)", example = "Mobile Phones", maxLength = 120)
        @NotBlank @Size(max = 120) String nameEn,

        @Schema(description = "URL slug; lowercased and unique (language-neutral)", example = "phones", maxLength = 140)
        @NotBlank @Size(max = 140) String slug,

        @Schema(description = "Optional description (Azerbaijani)", nullable = true)
        @Size(max = 2000) String descriptionAz,

        @Schema(description = "Optional description (English)", nullable = true)
        @Size(max = 2000) String descriptionEn,

        @Schema(description = "Parent category id; null = make this a root category", nullable = true)
        UUID parentId,

        @Schema(description = "Active flag; null leaves the current status unchanged", example = "true", nullable = true)
        Boolean active
) {
}
