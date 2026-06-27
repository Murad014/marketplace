package com.azercell.marketplace.orders.infrastructure.persistence.repository;

import com.azercell.marketplace.orders.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    Page<OrderJpaEntity> findByUserId(UUID userId, Pageable pageable);
}