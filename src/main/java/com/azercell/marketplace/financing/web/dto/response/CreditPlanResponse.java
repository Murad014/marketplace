package com.azercell.marketplace.financing.web.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CreditPlanResponse(
        UUID id,
        String name,
        int months,
        BigDecimal interestRate,
        String type,
        String status
) {
}