package com.azercell.marketplace.inventory.application.service;

import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.inventory.web.dto.request.AdjustStockRequest;
import com.azercell.marketplace.inventory.web.dto.request.RestockRequest;
import com.azercell.marketplace.inventory.web.dto.response.InventoryMovementResponse;
import com.azercell.marketplace.inventory.web.dto.response.InventoryResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    /** Seed (or top up) on-hand stock for a variant in a warehouse, recording a RESTOCK movement. */
    void seedStock(UUID warehouseId, UUID variantId, int quantity, String reference);

    /** Hold stock for an order (records a RESERVE movement). */
    void reserve(UUID warehouseId, UUID variantId, int quantity, String reference);

    /** Release previously reserved stock back to available (records a RELEASE movement). */
    void release(UUID warehouseId, UUID variantId, int quantity, String reference);

    /** Consume reserved stock on fulfilment (records a SALE movement). */
    void ship(UUID warehouseId, UUID variantId, int quantity, String reference);

    /** Add stock to an existing inventory record (records a RESTOCK movement). */
    InventoryResponse restock(RestockRequest request);

    /** Correct on-hand quantity to an absolute value (records a CORRECTION movement for the delta). */
    InventoryResponse adjustQuantity(AdjustStockRequest request);

    List<InventoryResponse> getByVariant(UUID variantId);

    PageResponse<InventoryMovementResponse> getMovements(UUID variantId, int page, int size);
}