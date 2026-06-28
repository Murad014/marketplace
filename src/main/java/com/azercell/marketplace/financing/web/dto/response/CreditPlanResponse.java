package com.azercell.marketplace.financing.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "A credit (installment) plan")
public record CreditPlanResponse(
        @Schema(description = "Plan id") UUID id,
        @Schema(description = "Name", example = "12 months") String name,
        @Schema(description = "Installment count (months)", example = "12") int months,
        @Schema(description = "Flat interest rate percent", example = "5.00") BigDecimal interestRate,
        @Schema(description = "Classification", example = "DEFAULT", allowableValues = {"DEFAULT", "OPTIONAL"}) String type,
        @Schema(description = "Lifecycle status", example = "ACTIVE", allowableValues = {"ACTIVE", "IN_ACTIVE"}) String status
) {
}
