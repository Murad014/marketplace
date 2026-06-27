package com.azercell.marketplace.orders.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String orderNumber,
        UUID userId,
        UUID warehouseId,
        String status,
        BigDecimal subtotalAmount,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        String currency,
        LocalDateTime placedAt,
        List<Item> items,
        List<StatusHistory> statusHistory
) {
    public record Item(
            UUID id,
            UUID variantId,
            String productName,
            String productSku,
            String colorName,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            int quantity,
            BigDecimal lineTotal,
            boolean wasPromo,
            String promoLabel
    ) {}

    public record StatusHistory(
            String fromStatus,
            String toStatus,
            String note,
            UUID changedBy,
            LocalDateTime changedAt
    ) {}
}