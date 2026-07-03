package com.azercell.marketplace.content.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "A published FAQ entry, localized to the request's Accept-Language (public view)")
public record FaqResponse(
        @Schema(description = "FAQ id") UUID id,
        @Schema(description = "Question in the requested language") String question,
        @Schema(description = "Answer in the requested language") String answer,
        @Schema(description = "Section label; null = ungrouped", nullable = true) String group,
        @Schema(description = "Listing order (ascending)", example = "10") int displayOrder
) {
}
