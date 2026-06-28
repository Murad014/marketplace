package com.azercell.marketplace.orders.web.dto.request;

import com.azercell.marketplace.orders.domain.vo.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateOrderStatusRequest(
        @NotNull OrderStatus status,
        @Size(max = 255) String note
) {
}