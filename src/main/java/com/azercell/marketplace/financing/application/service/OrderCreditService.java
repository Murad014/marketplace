package com.azercell.marketplace.financing.application.service;

import com.azercell.marketplace.financing.web.dto.response.OrderCreditResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderCreditService {

    /** Create the financing agreement + installment schedule for an order. */
    OrderCreditResponse createForOrder(UUID orderId, UUID creditPlanId, BigDecimal principal);

    OrderCreditResponse getByOrderId(UUID orderId);

    OrderCreditResponse getById(UUID id);

    /** Pay the earliest unpaid installment. */
    OrderCreditResponse payNextInstallment(UUID orderCreditId);
}