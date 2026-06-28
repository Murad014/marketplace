package com.azercell.marketplace.financing.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "A financing agreement for an order, with its installment schedule")
public record OrderCreditResponse(
        UUID id,
        UUID orderId,
        UUID creditPlanId,
        int months,
        BigDecimal interestRate,
        BigDecimal principalAmount,
        BigDecimal interestAmount,
        BigDecimal totalPayable,
        BigDecimal installmentAmount,
        BigDecimal paidAmount,
        String status,
        LocalDateTime startedAt,
        List<InstallmentView> installments
) {
    public record InstallmentView(
            UUID id,
            int sequenceNo,
            LocalDate dueDate,
            BigDecimal amount,
            String status,
            BigDecimal paidAmount,
            LocalDateTime paidAt
    ) {}
}
