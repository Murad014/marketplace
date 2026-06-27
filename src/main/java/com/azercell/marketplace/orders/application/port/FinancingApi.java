package com.azercell.marketplace.orders.application.port;

import java.math.BigDecimal;
import java.util.UUID;

/** Orders' view of financing: open a credit agreement for an order against a chosen plan. */
public interface FinancingApi {
    void createOrderCredit(UUID orderId, UUID creditPlanId, BigDecimal principal);
}