package com.azercell.marketplace.orders.application.port;

import com.azercell.marketplace.orders.domain.aggregate.OrderStatusHistory;

import java.util.List;
import java.util.UUID;

public interface OrderStatusHistoryRepository {
    OrderStatusHistory save(OrderStatusHistory history);
    List<OrderStatusHistory> findByOrderId(UUID orderId);
}