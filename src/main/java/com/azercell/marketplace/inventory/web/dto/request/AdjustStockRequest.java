package com.azercell.marketplace.inventory.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Set the absolute on-hand quantity for a variant in a warehouse (stock correction)")
public record AdjustStockRequest(
        @Schema(description = "Warehouse id") @NotNull UUID warehouseId,
        @Schema(description = "Variant id") @NotNull UUID variantId,
        @Schema(description = "New absolute quantity (≥ 0)", example = "100") @NotNull @PositiveOrZero Integer newQuantity,
        @Schema(description = "Optional free-text reference/note", example = "Stock count 2026-06", nullable = true)
        @Size(max = 100) String reference
) {
}
