package com.azercell.marketplace.financing.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "A financing option for a given price, e.g. \"12 months × 157.50 AZN\"")
public record InstallmentOptionResponse(
        @Schema(description = "Credit plan id") UUID planId,
        @Schema(description = "Plan name", example = "12 months") String name,
        @Schema(description = "Installment count (months)", example = "12") int months,
        @Schema(description = "Interest rate percent", example = "5.00") BigDecimal interestRate,
        @Schema(description = "Monthly installment amount", example = "157.50") BigDecimal monthlyInstallment,
        @Schema(description = "Total interest added", example = "89.99") BigDecimal interestAmount,
        @Schema(description = "Total payable (principal + interest)", example = "1889.99") BigDecimal totalPayable
) {
}
