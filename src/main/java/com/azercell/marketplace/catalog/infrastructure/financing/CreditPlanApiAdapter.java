package com.azercell.marketplace.catalog.infrastructure.financing;

import com.azercell.marketplace.catalog.application.port.CreditPlanApi;
import com.azercell.marketplace.catalog.application.port.CreditPlanQuote;
import com.azercell.marketplace.financing.application.port.CreditPlanRepository;
import com.azercell.marketplace.financing.domain.aggregate.CreditPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Anti-corruption adapter: implements the catalog's {@link CreditPlanApi} by delegating to the
 * financing context. Translates financing's CreditPlan into catalog-facing quotes/ids so the
 * catalog never touches financing internals directly.
 */
@Component
@RequiredArgsConstructor
public class CreditPlanApiAdapter implements CreditPlanApi {

    private static final String FULL_PAYMENT_PLAN_NAME = "Full payment";

    private final CreditPlanRepository creditPlanRepository;

    @Override
    public UUID fullPaymentPlanId() {
        return creditPlanRepository.findByName(FULL_PAYMENT_PLAN_NAME)
                .map(CreditPlan::getId)
                .orElse(null);
    }

    @Override
    public boolean existsAndActive(UUID creditPlanId) {
        return creditPlanId != null && creditPlanRepository.existsActive(creditPlanId);
    }

    @Override
    public Set<UUID> defaultPlanIds() {
        return creditPlanRepository.findActiveDefaults().stream()
                .map(CreditPlan::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<CreditPlanQuote> quoteFor(BigDecimal price, Collection<UUID> planIds) {
        if (price == null || planIds == null || planIds.isEmpty())
            return List.of();

        return planIds.stream()
                .map(creditPlanRepository::findById)
                .flatMap(Optional::stream)
                .filter(CreditPlan::isActive)
                .sorted(Comparator.comparingInt(CreditPlan::getMonths))
                .map(plan -> new CreditPlanQuote(
                        plan.getId(),
                        plan.getName(),
                        plan.getMonths(),
                        plan.getInterestRate().value(),
                        plan.monthlyInstallment(price),
                        plan.totalPayable(price)))
                .toList();
    }
}