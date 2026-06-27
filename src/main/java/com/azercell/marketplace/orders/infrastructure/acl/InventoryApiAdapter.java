package com.azercell.marketplace.orders.infrastructure.acl;

import com.azercell.marketplace.inventory.application.port.WarehouseRepository;
import com.azercell.marketplace.inventory.application.service.InventoryService;
import com.azercell.marketplace.inventory.domain.aggregate.Warehouse;
import com.azercell.marketplace.orders.application.port.InventoryApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** Delegates order stock movements to the inventory context. */
@Component
@RequiredArgsConstructor
public class InventoryApiAdapter implements InventoryApi {

    private final InventoryService inventoryService;
    private final WarehouseRepository warehouseRepository;

    @Override
    public UUID primaryWarehouseId() {
        return warehouseRepository.findPrimary().map(Warehouse::getId).orElse(null);
    }

    @Override
    public void reserve(UUID warehouseId, UUID variantId, int quantity, String reference) {
        inventoryService.reserve(warehouseId, variantId, quantity, reference);
    }

    @Override
    public void release(UUID warehouseId, UUID variantId, int quantity, String reference) {
        inventoryService.release(warehouseId, variantId, quantity, reference);
    }

    @Override
    public void ship(UUID warehouseId, UUID variantId, int quantity, String reference) {
        inventoryService.ship(warehouseId, variantId, quantity, reference);
    }
}
