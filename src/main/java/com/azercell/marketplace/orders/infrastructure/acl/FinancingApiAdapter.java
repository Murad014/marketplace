package com.azercell.marketplace.orders.infrastructure.acl;

import com.azercell.marketplace.financing.application.service.OrderCreditService;
import com.azercell.marketplace.orders.application.port.FinancingApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/** Delegates order-credit creation to the financing context. */
@Component
@RequiredArgsConstructor
public class FinancingApiAdapter implements FinancingApi {

    private final OrderCreditService orderCreditService;

    @Override
    public void createOrderCredit(UUID orderId, UUID creditPlanId, BigDecimal principal) {
        orderCreditService.createForOrder(orderId, creditPlanId, principal);
    }
}