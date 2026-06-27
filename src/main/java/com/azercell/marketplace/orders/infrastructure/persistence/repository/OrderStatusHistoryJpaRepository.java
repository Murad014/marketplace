package com.azercell.marketplace.orders.infrastructure.persistence.repository;

import com.azercell.marketplace.orders.infrastructure.persistence.entity.OrderStatusHistoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderStatusHistoryJpaRepository extends JpaRepository<OrderStatusHistoryJpaEntity, UUID> {
    List<OrderStatusHistoryJpaEntity> findByOrderIdOrderByCreatedDateAsc(UUID orderId);
}
