package com.azercell.marketplace.catalog.infrastructure.persistence.repository;

import com.azercell.marketplace.catalog.infrastructure.persistence.entity.ColorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ColorJpaRepository extends JpaRepository<ColorJpaEntity, UUID> {
    Optional<ColorJpaEntity> findByName(String name);
}
