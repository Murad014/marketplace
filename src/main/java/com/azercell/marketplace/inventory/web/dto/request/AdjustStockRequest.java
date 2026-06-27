package com.azercell.marketplace.inventory.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AdjustStockRequest(
        @NotNull UUID warehouseId,
        @NotNull UUID variantId,
        @NotNull @PositiveOrZero Integer newQuantity,
        @Size(max = 100) String reference
) {
}