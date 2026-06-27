package com.azercell.marketplace.financing.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.financing.domain.Installment;
import com.azercell.marketplace.financing.domain.vo.CreditStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The financing agreement on an order (one per order). Snapshots the plan's months + rate at
 * signing, computes the flat-interest total, and owns the generated installment schedule.
 */
@Getter
public class OrderCredit {

    private final UUID id;
    private final UUID orderId;
    private final UUID creditPlanId;
    private final int months;
    private final BigDecimal interestRate;       // snapshot of the plan rate %
    private final BigDecimal principalAmount;    // order total financed
    private final BigDecimal interestAmount;
    private final BigDecimal totalPayable;
    private final BigDecimal installmentAmount;
    private BigDecimal paidAmount;
    private CreditStatus status;
    private final LocalDateTime startedAt;
    private final List<Installment> installments;

    private OrderCredit(UUID id, UUID orderId, UUID creditPlanId, int months, BigDecimal interestRate,
                        BigDecimal principalAmount, BigDecimal interestAmount, BigDecimal totalPayable,
                        BigDecimal installmentAmount, BigDecimal paidAmount, CreditStatus status,
                        LocalDateTime startedAt, List<Installment> installments) {
        this.id = id;
        this.orderId = orderId;
        this.creditPlanId = creditPlanId;
        this.months = months;
        this.interestRate = interestRate;
        this.principalAmount = principalAmount;
        this.interestAmount = interestAmount;
        this.totalPayable = totalPayable;
        this.installmentAmount = installmentAmount;
        this.paidAmount = paidAmount;
        this.status = status;
        this.startedAt = startedAt;
        this.installments = new ArrayList<>(installments);
    }

    public static OrderCredit create(UUID orderId, UUID creditPlanId, int months,
                                     BigDecimal interestRate, BigDecimal principal) {
        if (orderId == null || creditPlanId == null) throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        if (months <= 0) throw new DomainException(ErrorCode.CREDIT_PLAN_MONTHS_INVALID);
        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0)
            throw new DomainException(ErrorCode.PRODUCT_BASE_PRICE_INVALID);

        var interest = principal.multiply(interestRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        var total = principal.add(interest);
        var perMonth = total.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);

        var id = UUID.randomUUID();
        var startedAt = LocalDateTime.now();
        var installments = scheduleInstallments(months, total, perMonth, startedAt.toLocalDate());

        return new OrderCredit(id, orderId, creditPlanId, months, interestRate, principal, interest,
                total, perMonth, BigDecimal.ZERO, CreditStatus.ACTIVE, startedAt, installments);
    }

    public static OrderCredit rehydrate(UUID id, UUID orderId, UUID creditPlanId, int months,
                                        BigDecimal interestRate, BigDecimal principalAmount,
                                        BigDecimal interestAmount, BigDecimal totalPayable,
                                        BigDecimal installmentAmount, BigDecimal paidAmount,
                                        CreditStatus status, LocalDateTime startedAt,
                                        List<Installment> installments) {
        return new OrderCredit(id, orderId, creditPlanId, months, interestRate, principalAmount,
                interestAmount, totalPayable, installmentAmount, paidAmount, status, startedAt, installments);
    }

    /** Settle the earliest unpaid installment; completes the agreement once everything is paid. */
    public Installment payNextInstallment() {
        if (status != CreditStatus.ACTIVE)
            throw new DomainException(ErrorCode.ORDER_CREDIT_NOT_ACTIVE);

        var next = installments.stream()
                .filter(i -> !i.isSettled())
                .min(Comparator.comparingInt(Installment::getSequenceNo))
                .orElseThrow(() -> new DomainException(ErrorCode.ORDER_CREDIT_NOT_ACTIVE));

        next.markPaid();
        this.paidAmount = this.paidAmount.add(next.getAmount());

        if (installments.stream().allMatch(Installment::isSettled))
            this.status = CreditStatus.COMPLETED;

        return next;
    }

    public List<Installment> getInstallments() {
        return List.copyOf(installments);
    }

    private static List<Installment> scheduleInstallments(int months, BigDecimal total,
                                                          BigDecimal perMonth, LocalDate start) {
        List<Installment> result = new ArrayList<>();
        BigDecimal allocated = BigDecimal.ZERO;
        for (int seq = 1; seq <= months; seq++) {
            // Last installment absorbs the rounding remainder so the schedule sums to total exactly.
            BigDecimal amount = (seq == months) ? total.subtract(allocated) : perMonth;
            allocated = allocated.add(amount);
            result.add(Installment.schedule(seq, start.plusMonths(seq), amount));
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCredit other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}