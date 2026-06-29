package com.azercell.marketplace.catalog.application.port;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Optional storefront filters for the active-product listing. Any field may be null (ignored).
 * Price bounds apply to the effective selling price = COALESCE(promoPrice, basePrice).
 */
public record ProductFilter(
        String name,          // case-insensitive partial match on product name
        UUID categoryId,      // exact category match
        BigDecimal minPrice,  // inclusive lower bound on selling price
        BigDecimal maxPrice   // inclusive upper bound on selling price
) {
    public boolean isEmpty() {
        return name == null && categoryId == null && minPrice == null && maxPrice == null;
    }
}
