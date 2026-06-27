package com.azercell.marketplace.inventory.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.inventory.application.port.InventoryRepository;
import com.azercell.marketplace.inventory.domain.aggregate.Inventory;
import com.azercell.marketplace.inventory.infrastructure.persistence.mapper.InventoryMapper;
import com.azercell.marketplace.inventory.infrastructure.persistence.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InventoryJpaRepositoryAdapter implements InventoryRepository {
    private final InventoryJpaRepository inventoryJpaRepository;

    @Override
    public Inventory save(Inventory inventory) {
        var saved = inventoryJpaRepository.save(InventoryMapper.toJpaEntity(inventory));
        return InventoryMapper.toDomain(saved);
    }

    @Override
    public Optional<Inventory> findById(UUID id) {
        return inventoryJpaRepository.findById(id).map(InventoryMapper::toDomain);
    }

    @Override
    public Optional<Inventory> findByWarehouseAndVariant(UUID warehouseId, UUID variantId) {
        return inventoryJpaRepository.findByStockIdAndVariantId(warehouseId, variantId)
                .map(InventoryMapper::toDomain);
    }

    @Override
    public List<Inventory> findByVariant(UUID variantId) {
        return inventoryJpaRepository.findByVariantId(variantId).stream()
                .map(InventoryMapper::toDomain)
                .toList();
    }
}