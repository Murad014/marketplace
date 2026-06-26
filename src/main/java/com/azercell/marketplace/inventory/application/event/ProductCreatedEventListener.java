package com.azercell.marketplace.inventory.application.event;

import com.azercell.marketplace.catalog.application.event.ProductCreatedEvent;
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
 * stock rows commit together. If no primary warehouse is configured, seeding is skipped with a
 * warning rather than failing the product creation.
 */
@Component
@RequiredArgsConstructor
public class  ProductCreatedEventListener {

    private static final Logger log = LoggerFactory.getLogger(ProductCreatedEventListener.class);

    private final WarehouseRepository warehouseRepository;
    private final InventoryService inventoryService;

    @EventListener
    public void onProductCreated(ProductCreatedEvent event) {
        var primary = warehouseRepository.findPrimary();
        if (primary.isEmpty()) {
            log.warn("No primary warehouse configured — skipping stock seeding for product {}",
                    event.productId());
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