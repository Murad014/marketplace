package com.azercell.marketplace.inventory.application.event;

import com.azercell.marketplace.catalog.application.event.ProductCreatedEvent;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.inventory.application.port.WarehouseRepository;
import com.azercell.marketplace.inventory.application.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Seeds initial inventory when the catalog reports a new product. Runs synchronously in the
 * same transaction as product creation (default {@link EventListener}), so the product and its
 * stock rows commit together.
 *
 * <p>If the worker entered stock but no primary warehouse is configured, we fail the whole
 * operation (rolling product creation back) rather than silently dropping the counts — recording
 * "success" while losing the entered stock would mislead the worker. When there is nothing to seed
 * (all quantities zero), product creation proceeds.
 */
@Component
@RequiredArgsConstructor
public class  ProductCreatedEventListener {

    private static final Logger log = LoggerFactory.getLogger(ProductCreatedEventListener.class);

    private final WarehouseRepository warehouseRepository;
    private final InventoryService inventoryService;

    @EventListener
    public void onProductCreated(ProductCreatedEvent event) {
        boolean hasStockToSeed = event.variantStocks().stream().anyMatch(vs -> vs.quantity() > 0);

        var primary = warehouseRepository.findPrimary();
        if (primary.isEmpty()) {
            if (hasStockToSeed) {
                // Same transaction as product creation: throwing rolls the product back too,
                // so the worker is told immediately instead of losing the stock silently.
                log.error("No primary warehouse configured but product {} was created with stock — "
                        + "rolling back so the entered stock is not lost", event.productId());
                throw new DomainException(ErrorCode.INVENTORY_NO_PRIMARY_WAREHOUSE);
            }
            log.warn("No primary warehouse configured — nothing to seed for product {}", event.productId());
            return;
        }

        var warehouseId = primary.get().getId();
        var reference = "PRODUCT_CREATE:" + event.productId();
        for (var vs : event.variantStocks()) {
            if (vs.quantity() > 0) {
                inventoryService.seedStock(warehouseId, vs.variantId(), vs.quantity(), reference);
            }
        }
        log.debug("Seeded stock for product {} into warehouse {}", event.productId(), warehouseId);
    }
}