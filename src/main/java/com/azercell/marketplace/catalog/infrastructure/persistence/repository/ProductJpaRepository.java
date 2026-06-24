package com.azercell.marketplace.catalog.infrastructure.persistence.repository;

import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
    boolean existsBySku(String sku);
    Page<ProductJpaEntity> findByStatus(Status status, Pageable pageable);
}
