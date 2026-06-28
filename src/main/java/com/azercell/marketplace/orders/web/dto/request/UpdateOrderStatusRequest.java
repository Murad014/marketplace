package com.azercell.marketplace.orders.web.dto.request;

import com.azercell.marketplace.orders.domain.vo.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to transition an order to a new status")
public record UpdateOrderStatusRequest(
        @Schema(description = "Target status", example = "CONFIRMED",
                allowableValues = {"PENDING", "CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"})
        @NotNull OrderStatus status,
        @Schema(description = "Optional note recorded in the status history", example = "payment received", nullable = true)
        @Size(max = 255) String note
) {
}
