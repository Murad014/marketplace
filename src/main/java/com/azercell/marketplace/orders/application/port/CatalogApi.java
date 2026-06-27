package com.azercell.marketplace.orders.application.port;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/** Orders' view of the catalog: resolve the pricing/snapshot for an order line by variant id. */
public interface CatalogApi {

    Optional<OrderLineInfo> findOrderLineInfo(UUID variantId);

    record OrderLineInfo(
            UUID variantId,
            String productName,
            String productSku,
            String colorName,
            BigDecimal originalPrice,   // base price before promo
            BigDecimal unitPrice,       // effective selling price (after promo)
            boolean wasPromo,
            String promoLabel
    ) {}
}