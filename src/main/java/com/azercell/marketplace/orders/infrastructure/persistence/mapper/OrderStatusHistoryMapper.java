package com.azercell.marketplace.orders.infrastructure.persistence.mapper;

import com.azercell.marketplace.orders.domain.aggregate.OrderStatusHistory;
import com.azercell.marketplace.orders.infrastructure.persistence.entity.OrderStatusHistoryJpaEntity;

public class OrderStatusHistoryMapper {

    public static OrderStatusHistoryJpaEntity toJpaEntity(OrderStatusHistory history) {
        var e = new OrderStatusHistoryJpaEntity();
        e.setId(history.getId());
        e.setOrderId(history.getOrderId());
        e.setFromStatus(history.getFromStatus());
        e.setToStatus(history.getToStatus());
        e.setNote(history.getNote());
        e.setChangedBy(history.getChangedBy());
        return e;
    }

    public static OrderStatusHistory toDomain(OrderStatusHistoryJpaEntity e) {
        return OrderStatusHistory.rehydrate(
                e.getId(),
                e.getOrderId(),
                e.getFromStatus(),
                e.getToStatus(),
                e.getNote(),
                e.getChangedBy(),
                e.getCreatedDate());
    }
}