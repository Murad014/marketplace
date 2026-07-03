package com.azercell.marketplace.content.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to create a bilingual FAQ entry")
public record CreateFaqRequest(
        @Schema(description = "Question (Azerbaijani)", example = "Sifarişimi necə izləyə bilərəm?", maxLength = 1000)
        @NotBlank @Size(max = 1000) String questionAz,

        @Schema(description = "Question (English)", example = "How can I track my order?", maxLength = 1000)
        @NotBlank @Size(max = 1000) String questionEn,

        @Schema(description = "Answer (Azerbaijani)")
        @NotBlank @Size(max = 5000) String answerAz,

        @Schema(description = "Answer (English)")
        @NotBlank @Size(max = 5000) String answerEn,

        @Schema(description = "Optional section label to group entries", example = "Orders", nullable = true)
        @Size(max = 120) String group,

        @Schema(description = "Listing order (ascending); defaults to 0", example = "10", nullable = true)
        Integer displayOrder
) {
}
