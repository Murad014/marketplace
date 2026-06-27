package com.azercell.marketplace.financing.infrastructure.bootstrap;

import com.azercell.marketplace.financing.application.port.CreditPlanRepository;
import com.azercell.marketplace.financing.domain.aggregate.CreditPlan;
import com.azercell.marketplace.financing.domain.vo.CreditPlanType;
import com.azercell.marketplace.financing.domain.vo.InterestRate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds the standard installment plans on a fresh database so production comes up ready to
 * finance. Idempotent: runs only when the credit_plans table is empty, so it never duplicates
 * on restart or overwrites plans an admin has since customised.
 *
 * Disable with: app.financing.seed-default-plans=false
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.financing.seed-default-plans", havingValue = "true", matchIfMissing = true)
public class DefaultCreditPlanSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DefaultCreditPlanSeeder.class);

    private final CreditPlanRepository creditPlanRepository;

    private record DefaultPlan(String name, int months, String rate) {}

    // Business defaults: full payment (0%), 3-6 months @ 2%, 7-12 months @ 5%.
    private static final List<DefaultPlan> DEFAULTS = List.of(
            new DefaultPlan("Full payment", 1, "0"),
            new DefaultPlan("3 months", 3, "2"),
            new DefaultPlan("6 months", 6, "2"),
            new DefaultPlan("9 months", 9, "5"),
            new DefaultPlan("12 months", 12, "5")
    );

    @Override
    public void run(ApplicationArguments args) {
        if (!creditPlanRepository.findAll().isEmpty()) {
            log.debug("Credit plans already present — skipping default seed.");
            return;
        }

        DEFAULTS.forEach(d -> creditPlanRepository.save(
                CreditPlan.create(d.name(), d.months(), new InterestRate(new BigDecimal(d.rate())),
                        CreditPlanType.DEFAULT)));

        log.info("Seeded {} default credit plans.", DEFAULTS.size());
    }
}
