package com.azercell.marketplace.financing.domain.aggregate;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderCredit {
    private UUID id;
    private UUID orderId;
    private CreditPlan creditPlan;
    private int months;
    private BigDecimal interestRate;

}
