package com.azercell.marketplace.financing.domain;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.financing.domain.vo.InstallmentStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/** One scheduled monthly payment within an {@code OrderCredit}. */
@Getter
public class Installment {

    private final UUID id;
    private final int sequenceNo;
    private final LocalDate dueDate;
    private final BigDecimal amount;
    private InstallmentStatus status;
    private BigDecimal paidAmount;
    private LocalDateTime paidAt;

    private Installment(UUID id, int sequenceNo, LocalDate dueDate, BigDecimal amount,
                        InstallmentStatus status, BigDecimal paidAmount, LocalDateTime paidAt) {
        this.id = id;
        this.sequenceNo = sequenceNo;
        this.dueDate = dueDate;
        this.amount = amount;
        this.status = status;
        this.paidAmount = paidAmount;
        this.paidAt = paidAt;
    }

    public static Installment schedule(int sequenceNo, LocalDate dueDate, BigDecimal amount) {
        return new Installment(UUID.randomUUID(), sequenceNo, dueDate, amount,
                InstallmentStatus.PENDING, BigDecimal.ZERO, null);
    }

    public static Installment rehydrate(UUID id, int sequenceNo, LocalDate dueDate, BigDecimal amount,
                                        InstallmentStatus status, BigDecimal paidAmount, LocalDateTime paidAt) {
        return new Installment(id, sequenceNo, dueDate, amount, status, paidAmount, paidAt);
    }

    public boolean isSettled() {
        return status == InstallmentStatus.PAID || status == InstallmentStatus.WAIVED;
    }

    public void markPaid() {
        if (status == InstallmentStatus.PAID)
            throw new DomainException(ErrorCode.INSTALLMENT_ALREADY_PAID);
        this.status = InstallmentStatus.PAID;
        this.paidAmount = this.amount;
        this.paidAt = LocalDateTime.now();
    }
}