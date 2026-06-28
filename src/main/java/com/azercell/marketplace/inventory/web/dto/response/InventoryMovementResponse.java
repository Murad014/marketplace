package com.azercell.marketplace.inventory.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "A single stock movement (ledger entry)")
public record InventoryMovementResponse(
        @Schema(description = "Movement id") UUID id,
        @Schema(description = "Warehouse id") UUID warehouseId,
        @Schema(description = "Variant id") UUID variantId,
        @Schema(description = "Signed change (+restock, −reserve/sale)", example = "-1") int change,
        @Schema(description = "Movement type", example = "RESERVE",
                allowableValues = {"RESTOCK", "RESERVE", "RELEASE", "SALE", "CORRECTION"}) String type,
        @Schema(description = "Reference/note", example = "ORDER:ORD-20260628-D5BBC9", nullable = true) String reference,
        @Schema(description = "User who performed it; null for system/seed", nullable = true) UUID performedBy,
        @Schema(description = "When it was recorded") LocalDateTime createdAt
) {
}
