package com.azercell.marketplace.inventory.application.port;

import com.azercell.marketplace.inventory.domain.aggregate.Inventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository {
    Inventory save(Inventory inventory);
    Optional<Inventory> findById(UUID id);
    Optional<Inventory> findByWarehouseAndVariant(UUID warehouseId, UUID variantId);
    List<Inventory> findByVariant(UUID variantId);
}