package com.azercell.marketplace.financing.web.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

/** A financing option for a given price: e.g. "12 months × 87.50 AZN". */
public record InstallmentOptionResponse(
        UUID planId,
        String name,
        int months,
        BigDecimal interestRate,
        BigDecimal monthlyInstallment,
        BigDecimal interestAmount,
        BigDecimal totalPayable
) {
}