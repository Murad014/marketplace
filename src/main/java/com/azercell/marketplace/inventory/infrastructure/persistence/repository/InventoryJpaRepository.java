package com.azercell.marketplace.inventory.infrastructure.persistence.repository;

import com.azercell.marketplace.inventory.infrastructure.persistence.entity.InventoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryJpaRepository extends JpaRepository<InventoryJpaEntity, UUID> {
    Optional<InventoryJpaEntity> findByStockIdAndVariantId(UUID stockId, UUID variantId);
    List<InventoryJpaEntity> findByVariantId(UUID variantId);
}