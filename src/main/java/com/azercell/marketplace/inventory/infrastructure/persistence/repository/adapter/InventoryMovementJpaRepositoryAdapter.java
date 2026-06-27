package com.azercell.marketplace.inventory.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.inventory.application.port.InventoryMovementRepository;
import com.azercell.marketplace.inventory.domain.aggregate.InventoryMovement;
import com.azercell.marketplace.inventory.infrastructure.persistence.mapper.InventoryMovementMapper;
import com.azercell.marketplace.inventory.infrastructure.persistence.repository.InventoryMovementJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InventoryMovementJpaRepositoryAdapter implements InventoryMovementRepository {
    private final InventoryMovementJpaRepository movementJpaRepository;

    @Override
    public InventoryMovement save(InventoryMovement movement) {
        var saved = movementJpaRepository.save(InventoryMovementMapper.toJpaEntity(movement));
        return InventoryMovementMapper.toDomain(saved);
    }

    @Override
    public Page<InventoryMovement> findByVariant(UUID variantId, Pageable pageable) {
        return movementJpaRepository.findByVariantId(variantId, pageable)
                .map(InventoryMovementMapper::toDomain);
    }
}