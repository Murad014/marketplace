package com.azercell.marketplace.inventory.infrastructure.persistence.mapper;

import com.azercell.marketplace.inventory.domain.aggregate.InventoryMovement;
import com.azercell.marketplace.inventory.infrastructure.persistence.entity.InventoryMovementJpaEntity;

public class InventoryMovementMapper {

    public static InventoryMovementJpaEntity toJpaEntity(InventoryMovement movement) {
        var entity = new InventoryMovementJpaEntity();
        entity.setId(movement.getId());
        entity.setStockId(movement.getWarehouseId());
        entity.setVariantId(movement.getVariantId());
        entity.setChange(movement.getChange());
        entity.setType(movement.getType());
        entity.setReference(movement.getReference());
        entity.setPerformedBy(movement.getPerformedBy());
        return entity;
    }

    public static InventoryMovement toDomain(InventoryMovementJpaEntity entity) {
        return InventoryMovement.rehydrate(
                entity.getId(),
                entity.getStockId(),
                entity.getVariantId(),
                entity.getChange(),
                entity.getType(),
                entity.getReference(),
                entity.getPerformedBy(),
                entity.getCreatedDate());
    }
}