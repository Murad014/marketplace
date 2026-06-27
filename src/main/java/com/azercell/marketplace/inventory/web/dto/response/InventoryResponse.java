package com.azercell.marketplace.inventory.web.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record InventoryResponse(
        UUID id,
        UUID warehouseId,
        UUID variantId,
        int quantity,
        int reservedQuantity,
        int availableQuantity,
        BigDecimal sellerPrice,
        BigDecimal purchasePrice,
        int lowStockThreshold,
        boolean lowStock
) {
}