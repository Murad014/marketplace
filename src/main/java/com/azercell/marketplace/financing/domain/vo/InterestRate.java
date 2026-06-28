package com.azercell.marketplace.financing.domain.vo;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record InterestRate(BigDecimal value) {

    public InterestRate {
        if (value == null ||
                value.compareTo(BigDecimal.ZERO) < 0 ||
                value.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new DomainException(ErrorCode.INTEREST_RATE_INVALID);
        }
    }

    public BigDecimal asDecimal() {
        return value.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }
}