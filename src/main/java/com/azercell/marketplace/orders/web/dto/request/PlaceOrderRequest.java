package com.azercell.marketplace.orders.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

public record PlaceOrderRequest(
        UUID warehouseId,              // optional; defaults to the primary warehouse
        UUID creditPlanId,             // optional; null = pay in full, no financing agreement
        @NotEmpty @Valid List<Line> items
) {
    public record Line(
            @NotNull UUID variantId,
            @NotNull @Positive Integer quantity
    ) {}
}
