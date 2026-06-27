package com.azercell.marketplace.catalog.application.port;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Catalog's view of the financing context (anti-corruption boundary). Lets the catalog validate
 * credit-plan eligibility and render installment options without depending on financing internals.
 */
public interface CreditPlanApi {
    UUID fullPaymentPlanId();
    boolean existsAndActive(UUID creditPlanId);

    /** Active plans tagged DEFAULT — the standard offering applied when a product specifies none. */
    Set<UUID> defaultPlanIds();

    /** Installment options for a price across the given plan ids (inactive/unknown ids dropped). */
    List<CreditPlanQuote> quoteFor(BigDecimal price, Collection<UUID> planIds);
}
