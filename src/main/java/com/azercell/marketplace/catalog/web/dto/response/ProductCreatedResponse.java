package com.azercell.marketplace.catalog.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Result of creating a product")
public record ProductCreatedResponse(
        @Schema(description = "Localized success message", example = "Created successfully") String message,
        @Schema(description = "Id of the newly created product") String id
) {
}
