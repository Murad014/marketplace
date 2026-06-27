package com.azercell.marketplace.financing.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.financing.domain.vo.CreditPlanType;
import com.azercell.marketplace.financing.domain.vo.InterestRate;
import com.azercell.marketplace.financing.domain.vo.Status;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

@Getter
public class CreditPlan {

    private final UUID id;
    private String name;
    private int months;
    private InterestRate interestRate;
    private CreditPlanType type;
    private Status status;

    private CreditPlan(UUID id, String name, int months, InterestRate interestRate,
                       CreditPlanType type, Status status) {
        validateName(name);
        validateMonths(months);
        validateInterestRate(interestRate);

        this.id = id;
        this.name = name.trim();
        this.months = months;
        this.interestRate = interestRate;
        this.type = type == null ? CreditPlanType.OPTIONAL : type;
        this.status = status == null ? Status.ACTIVE : status;
    }

    public static CreditPlan create(String name, int months, InterestRate interestRate, CreditPlanType type) {
        return new CreditPlan(UUID.randomUUID(), name, months, interestRate, type, Status.ACTIVE);
    }

    public static CreditPlan rehydrate(UUID id, String name, int months, InterestRate interestRate,
                                       CreditPlanType type, Status status) {
        if (id == null) throw new DomainException(ErrorCode.CREDIT_PLAN_NOT_FOUND);
        return new CreditPlan(id, name, months, interestRate, type, status);
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    public void changeMonths(int months) {
        validateMonths(months);
        this.months = months;
    }

    public void changeInterestRate(InterestRate interestRate) {
        validateInterestRate(interestRate);
        this.interestRate = interestRate;
    }

    public void changeType(CreditPlanType type) {
        if (type == null) throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        this.type = type;
    }

    public boolean isDefault() {
        return type == CreditPlanType.DEFAULT;
    }

    public void activate() {
        this.status = Status.ACTIVE;
    }

    public void deactivate() {
        this.status = Status.IN_ACTIVE;
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    /** Flat interest charged over the whole term: principal × rate%. */
    public BigDecimal interestAmount(BigDecimal price) {
        requirePositive(price);
        return price.multiply(interestRate.value())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal totalPayable(BigDecimal price) {
        return price.add(interestAmount(price));
    }

    public BigDecimal monthlyInstallment(BigDecimal price) {
        return totalPayable(price).divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
    }

    // <editor-fold desc="privateHelperMethods">
    private static void requirePositive(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            throw new DomainException(ErrorCode.PRODUCT_BASE_PRICE_INVALID);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank())
            throw new DomainException(ErrorCode.CREDIT_PLAN_NAME_REQUIRED);
    }

    private static void validateMonths(int months) {
        if (months <= 0)
            throw new DomainException(ErrorCode.CREDIT_PLAN_MONTHS_INVALID);
    }

    private static void validateInterestRate(InterestRate interestRate) {
        if (interestRate == null)
            throw new DomainException(ErrorCode.CREDIT_PLAN_RATE_INVALID);
    }
    // </editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditPlan other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}