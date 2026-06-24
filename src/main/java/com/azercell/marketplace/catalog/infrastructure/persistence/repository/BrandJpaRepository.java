package com.azercell.marketplace.catalog.infrastructure.persistence.repository;

import com.azercell.marketplace.catalog.infrastructure.persistence.entity.BrandJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BrandJpaRepository extends JpaRepository<BrandJpaEntity, UUID> {
}
