package com.azercell.marketplace.orders.application.port;

import java.util.UUID;

/** Orders' view of inventory: resolve the fulfilling warehouse and move stock for the order lifecycle. */
public interface InventoryApi {

    /** The default fulfilling warehouse; null if none is configured. */
    UUID primaryWarehouseId();

    void reserve(UUID warehouseId, UUID variantId, int quantity, String reference);

    void release(UUID warehouseId, UUID variantId, int quantity, String reference);

    void ship(UUID warehouseId, UUID variantId, int quantity, String reference);
}