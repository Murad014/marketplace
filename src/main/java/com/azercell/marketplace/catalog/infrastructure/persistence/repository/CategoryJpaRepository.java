package com.azercell.marketplace.catalog.infrastructure.persistence.repository;

import com.azercell.marketplace.catalog.infrastructure.persistence.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, UUID> {
}
