package com.azercell.marketplace.catalog.infrastructure.persistence.repository;

import com.azercell.marketplace.catalog.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
}
