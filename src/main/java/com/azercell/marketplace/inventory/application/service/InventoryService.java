package com.azercell.marketplace.inventory.application.service;

import com.azercell.marketplace.inventory.web.dto.response.InventoryResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    /** Seed (or top up) on-hand stock for a variant in a given warehouse. */
    void seedStock(UUID warehouseId, UUID variantId, int quantity);

    List<InventoryResponse> getByVariant(UUID variantId);
}