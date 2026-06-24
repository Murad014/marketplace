package com.azercell.marketplace.catalog.application.port;

import java.util.UUID;

public interface CreditPlanApi {
    UUID fullPaymentPlanId();
    boolean existsAndActive(UUID creditPlanId);
}
