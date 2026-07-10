package com.azercell.marketplace.catalog.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Payload to create a bilingual category")
public record CreateCategoryRequest(
        @Schema(description = "Display name (Azerbaijani)", example = "Telefonlar", maxLength = 120)
        @NotBlank @Size(max = 120) String nameAz,

        @Schema(description = "Display name (English)", example = "Phones", maxLength = 120)
        @NotBlank @Size(max = 120) String nameEn,

        @Schema(description = "URL slug; lowercased and unique (language-neutral)", example = "phones", maxLength = 140)
        @NotBlank @Size(max = 140) String slug,

        @Schema(description = "Optional description (Azerbaijani)", nullable = true)
        @Size(max = 2000) String descriptionAz,

        @Schema(description = "Optional description (English)", nullable = true)
        @Size(max = 2000) String descriptionEn,

        @Schema(description = "Optional parent category id; null/omitted = root category", nullable = true)
        UUID parentId
) {
}
