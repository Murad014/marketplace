package com.azercell.marketplace.inventory.infrastructure.persistence.repository;

import com.azercell.marketplace.inventory.infrastructure.persistence.entity.InventoryMovementJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryMovementJpaRepository extends JpaRepository<InventoryMovementJpaEntity, UUID> {
    Page<InventoryMovementJpaEntity> findByVariantId(UUID variantId, Pageable pageable);
}