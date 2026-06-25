package com.azercell.marketplace.catalog.application.event;

import java.util.List;
import java.util.UUID;

/**
 * Published by the catalog after a product is persisted. The inventory context listens and
 * seeds initial stock. The catalog owns this contract; downstream contexts depend on it.
 */
public record ProductCreatedEvent(UUID productId, List<VariantStock> variantStocks) {

    /** Initial on-hand quantity for a freshly created variant (from the request's stockCount). */
    public record VariantStock(UUID variantId, int quantity) {}
}