package com.azercell.marketplace.inventory.web.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record InventoryMovementResponse(
        UUID id,
        UUID warehouseId,
        UUID variantId,
        int change,
        String type,
        String reference,
        UUID performedBy,
        LocalDateTime createdAt
) {
}