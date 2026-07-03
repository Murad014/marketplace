package com.azercell.marketplace.content.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "A FAQ entry with both languages and visibility flag (admin view)")
public record FaqAdminResponse(
        @Schema(description = "FAQ id") UUID id,
        @Schema(description = "Question (Azerbaijani)") String questionAz,
        @Schema(description = "Question (English)") String questionEn,
        @Schema(description = "Answer (Azerbaijani)") String answerAz,
        @Schema(description = "Answer (English)") String answerEn,
        @Schema(description = "Section label; null = ungrouped", nullable = true) String group,
        @Schema(description = "Listing order (ascending)", example = "10") int displayOrder,
        @Schema(description = "Whether the entry is publicly visible", example = "true") boolean active
) {
}
