package com.azercell.marketplace.financing.application.port;

import com.azercell.marketplace.financing.domain.aggregate.CreditPlan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditPlanRepository {
    CreditPlan save(CreditPlan plan);
    Optional<CreditPlan> findById(UUID id);
    Optional<CreditPlan> findByName(String name);
    List<CreditPlan> findAll();
    List<CreditPlan> findAllActive();
    List<CreditPlan> findActiveDefaults();
    boolean existsActive(UUID id);
}