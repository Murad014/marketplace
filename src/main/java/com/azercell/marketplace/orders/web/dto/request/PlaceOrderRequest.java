package com.azercell.marketplace.orders.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

@Schema(description = "Payload to place an order. The buyer is taken from the authenticated token, not the body.")
public record PlaceOrderRequest(
        @Schema(description = "Fulfilment warehouse; null/omitted = primary warehouse", nullable = true)
        UUID warehouseId,
        @Schema(description = "Credit plan to finance the total; null = pay in full (no agreement)", nullable = true)
        UUID creditPlanId,
        @Schema(description = "Order lines (at least one)")
        @NotEmpty @Valid List<Line> items
) {
    @Schema(description = "One order line")
    public record Line(
            @Schema(description = "Variant id") @NotNull UUID variantId,
            @Schema(description = "Quantity (> 0)", example = "1") @NotNull @Positive Integer quantity
    ) {}
}
