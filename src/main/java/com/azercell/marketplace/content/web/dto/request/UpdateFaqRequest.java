package com.azercell.marketplace.content.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Full FAQ update. displayOrder/active are optional; null leaves them unchanged.")
public record UpdateFaqRequest(
        @Schema(description = "Question (Azerbaijani)", maxLength = 1000)
        @NotBlank @Size(max = 1000) String questionAz,

        @Schema(description = "Question (English)", maxLength = 1000)
        @NotBlank @Size(max = 1000) String questionEn,

        @Schema(description = "Answer (Azerbaijani)")
        @NotBlank @Size(max = 5000) String answerAz,

        @Schema(description = "Answer (English)")
        @NotBlank @Size(max = 5000) String answerEn,

        @Schema(description = "Optional section label; null/blank clears it", nullable = true)
        @Size(max = 120) String group,

        @Schema(description = "Listing order (ascending); null leaves it unchanged", nullable = true)
        Integer displayOrder,

        @Schema(description = "Visibility flag; null leaves the current status unchanged", example = "true", nullable = true)
        Boolean active
) {
}
