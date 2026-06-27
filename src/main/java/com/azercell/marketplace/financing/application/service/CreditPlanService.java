package com.azercell.marketplace.financing.application.service;

import com.azercell.marketplace.financing.web.dto.request.CreateCreditPlanRequest;
import com.azercell.marketplace.financing.web.dto.request.UpdateCreditPlanRequest;
import com.azercell.marketplace.financing.web.dto.response.CreditPlanResponse;
import com.azercell.marketplace.financing.web.dto.response.InstallmentOptionResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CreditPlanService {
    CreditPlanResponse create(CreateCreditPlanRequest request);
    CreditPlanResponse update(UUID id, UpdateCreditPlanRequest request);
    CreditPlanResponse getById(UUID id);
    List<CreditPlanResponse> list();
    CreditPlanResponse activate(UUID id);
    CreditPlanResponse deactivate(UUID id);

    /** Active plans rendered as installment options for a given price. */
    List<InstallmentOptionResponse> quote(BigDecimal price);
}