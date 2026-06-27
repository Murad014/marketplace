package com.azercell.marketplace.orders.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.orders.domain.vo.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/** An immutable record of one order status change (the {@code order_status_history} table). */
@Getter
public class OrderStatusHistory {

    private final UUID id;
    private final UUID orderId;
    private final OrderStatus fromStatus;   // null on the initial placement
    private final OrderStatus toStatus;
    private final String note;
    private final UUID changedBy;           // nullable until the users context exists
    private final LocalDateTime createdAt;  // from persistence auditing; null on a fresh record

    private OrderStatusHistory(UUID id, UUID orderId, OrderStatus fromStatus, OrderStatus toStatus,
                               String note, UUID changedBy, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.note = note;
        this.changedBy = changedBy;
        this.createdAt = createdAt;
    }

    public static OrderStatusHistory record(UUID orderId, OrderStatus fromStatus, OrderStatus toStatus,
                                            String note, UUID changedBy) {
        if (orderId == null || toStatus == null) throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        return new OrderStatusHistory(UUID.randomUUID(), orderId, fromStatus, toStatus,
                note == null || note.isBlank() ? null : note.trim(), changedBy, null);
    }

    public static OrderStatusHistory rehydrate(UUID id, UUID orderId, OrderStatus fromStatus,
                                               OrderStatus toStatus, String note, UUID changedBy,
                                               LocalDateTime createdAt) {
        return new OrderStatusHistory(id, orderId, fromStatus, toStatus, note, changedBy, createdAt);
    }
}