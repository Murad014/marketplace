package com.azercell.marketplace.catalog.application.port;

import java.math.BigDecimal;
import java.util.UUID;

/** Catalog-side view of a financing installment option for a specific price. */
public record CreditPlanQuote(
        UUID planId,
        String name,
        int months,
        BigDecimal interestRate,
        BigDecimal monthlyInstallment,
        BigDecimal totalPayable
) {
}