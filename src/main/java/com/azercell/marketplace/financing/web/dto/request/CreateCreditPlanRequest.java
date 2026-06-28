package com.azercell.marketplace.financing.web.dto.request;

import com.azercell.marketplace.financing.domain.vo.CreditPlanType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Payload to create a credit plan")
public record CreateCreditPlanRequest(
        @Schema(description = "Plan name", example = "12 months", maxLength = 80)
        @NotBlank @Size(max = 80) String name,
        @Schema(description = "Number of installments (months)", example = "12")
        @NotNull @Positive Integer months,
        @Schema(description = "Flat interest rate percent (0–100)", example = "5.00")
        @NotNull @PositiveOrZero @DecimalMax("100.00") BigDecimal interestRate,
        @Schema(description = "DEFAULT = part of the standard offering; null/OPTIONAL = opt-in", example = "DEFAULT", nullable = true)
        CreditPlanType type
) {
}
