package com.azercell.marketplace.inventory.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.inventory.application.port.WarehouseRepository;
import com.azercell.marketplace.inventory.domain.aggregate.Warehouse;
import com.azercell.marketplace.inventory.infrastructure.persistence.mapper.WarehouseMapper;
import com.azercell.marketplace.inventory.infrastructure.persistence.repository.WarehouseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WarehouseJpaRepositoryAdapter implements WarehouseRepository {
    private final WarehouseJpaRepository warehouseJpaRepository;

    @Override
    public Warehouse save(Warehouse warehouse) {
        var saved = warehouseJpaRepository.save(WarehouseMapper.toJpaEntity(warehouse));
        return WarehouseMapper.toDomain(saved);
    }

    @Override
    public Optional<Warehouse> findById(UUID id) {
        return warehouseJpaRepository.findById(id).map(WarehouseMapper::toDomain);
    }

    @Override
    public Optional<Warehouse> findByCode(String code) {
        return warehouseJpaRepository.findByCode(code).map(WarehouseMapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return warehouseJpaRepository.existsByCode(code);
    }

    @Override
    public Page<Warehouse> findAll(Pageable pageable) {
        return warehouseJpaRepository.findAll(pageable).map(WarehouseMapper::toDomain);
    }

    @Override
    public Optional<Warehouse> findPrimary() {
        return warehouseJpaRepository.findByPrimaryTrue().map(WarehouseMapper::toDomain);
    }
}