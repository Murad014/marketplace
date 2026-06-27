package com.azercell.marketplace.financing.web.dto.request;

import com.azercell.marketplace.financing.domain.vo.CreditPlanType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateCreditPlanRequest(
        @NotBlank @Size(max = 80) String name,
        @NotNull @Positive Integer months,
        @NotNull @PositiveOrZero @DecimalMax("100.00") BigDecimal interestRate,
        @NotNull CreditPlanType type
) {
}