package com.azercell.marketplace.inventory.application.port;

import com.azercell.marketplace.inventory.domain.aggregate.InventoryMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InventoryMovementRepository {
    InventoryMovement save(InventoryMovement movement);
    Page<InventoryMovement> findByVariant(UUID variantId, Pageable pageable);
}