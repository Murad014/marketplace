package com.azercell.marketplace.inventory.infrastructure.persistence.repository;

import com.azercell.marketplace.inventory.infrastructure.persistence.entity.WarehouseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WarehouseJpaRepository extends JpaRepository<WarehouseJpaEntity, UUID> {
    Optional<WarehouseJpaEntity> findByCode(String code);
    boolean existsByCode(String code);
    Optional<WarehouseJpaEntity> findByPrimaryTrue();
}