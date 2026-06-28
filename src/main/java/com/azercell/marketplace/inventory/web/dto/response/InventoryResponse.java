package com.azercell.marketplace.inventory.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Stock for a variant in one warehouse")
public record InventoryResponse(
        @Schema(description = "Inventory row id") UUID id,
        @Schema(description = "Warehouse id") UUID warehouseId,
        @Schema(description = "Variant id") UUID variantId,
        @Schema(description = "On-hand quantity", example = "100") int quantity,
        @Schema(description = "Reserved (held by open orders)", example = "5") int reservedQuantity,
        @Schema(description = "Available = quantity − reserved", example = "95") int availableQuantity,
        @Schema(description = "Seller price", nullable = true) BigDecimal sellerPrice,
        @Schema(description = "Purchase price", nullable = true) BigDecimal purchasePrice,
        @Schema(description = "Low-stock threshold", example = "10") int lowStockThreshold,
        @Schema(description = "Whether available is at/below the threshold", example = "false") boolean lowStock
) {
}
