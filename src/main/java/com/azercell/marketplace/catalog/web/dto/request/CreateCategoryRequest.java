package com.azercell.marketplace.catalog.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Payload to create a category")
public record CreateCategoryRequest(
        @Schema(description = "Display name", example = "Phones", maxLength = 120)
        @NotBlank @Size(max = 120) String name,

        @Schema(description = "URL slug; lowercased and unique", example = "phones", maxLength = 140)
        @NotBlank @Size(max = 140) String slug,

        @Schema(description = "Optional description", example = "Smartphones and accessories", nullable = true)
        @Size(max = 2000) String description,

        @Schema(description = "Optional parent category id; null/omitted = root category", nullable = true)
        UUID parentId
) {
}
