package com.azercell.marketplace.inventory.application.port;

import com.azercell.marketplace.inventory.domain.aggregate.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository {
    Warehouse save(Warehouse warehouse);
    Optional<Warehouse> findById(UUID id);
    Optional<Warehouse> findByCode(String code);
    boolean existsByCode(String code);
    Page<Warehouse> findAll(Pageable pageable);
    Optional<Warehouse> findPrimary();
}