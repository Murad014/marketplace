package com.azercell.marketplace.financing.infrastructure.persistence.repository;

import com.azercell.marketplace.financing.infrastructure.persistence.entity.OrderCreditJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderCreditJpaRepository extends JpaRepository<OrderCreditJpaEntity, UUID> {
    Optional<OrderCreditJpaEntity> findByOrderId(UUID orderId);
    boolean existsByOrderId(UUID orderId);
}
