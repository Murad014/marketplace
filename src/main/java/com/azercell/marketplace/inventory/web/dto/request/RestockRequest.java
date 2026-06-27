package com.azercell.marketplace.inventory.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RestockRequest(
        @NotNull UUID warehouseId,
        @NotNull UUID variantId,
        @NotNull @Positive Integer amount,
        @Size(max = 100) String reference
) {
}