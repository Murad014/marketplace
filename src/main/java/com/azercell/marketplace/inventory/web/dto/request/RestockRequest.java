package com.azercell.marketplace.inventory.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Increase stock for a variant in a warehouse")
public record RestockRequest(
        @Schema(description = "Warehouse id") @NotNull UUID warehouseId,
        @Schema(description = "Variant id") @NotNull UUID variantId,
        @Schema(description = "Amount to add (> 0)", example = "50") @NotNull @Positive Integer amount,
        @Schema(description = "Optional free-text reference/note", example = "PO-1234", nullable = true)
        @Size(max = 100) String reference
) {
}
