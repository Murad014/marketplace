package com.azercell.marketplace.orders.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.orders.application.port.OrderStatusHistoryRepository;
import com.azercell.marketplace.orders.domain.aggregate.OrderStatusHistory;
import com.azercell.marketplace.orders.infrastructure.persistence.mapper.OrderStatusHistoryMapper;
import com.azercell.marketplace.orders.infrastructure.persistence.repository.OrderStatusHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderStatusHistoryJpaRepositoryAdapter implements OrderStatusHistoryRepository {
    private final OrderStatusHistoryJpaRepository historyJpaRepository;

    @Override
    public OrderStatusHistory save(OrderStatusHistory history) {
        return OrderStatusHistoryMapper.toDomain(
                historyJpaRepository.save(OrderStatusHistoryMapper.toJpaEntity(history)));
    }

    @Override
    public List<OrderStatusHistory> findByOrderId(UUID orderId) {
        return historyJpaRepository.findByOrderIdOrderByCreatedDateAsc(orderId).stream()
                .map(OrderStatusHistoryMapper::toDomain)
                .toList();
    }
}