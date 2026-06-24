package com.azercell.marketplace.inventory.infrastructure.persistence.mapper;

import com.azercell.marketplace.inventory.domain.aggregate.Warehouse;
import com.azercell.marketplace.inventory.infrastructure.persistence.entity.WarehouseJpaEntity;

public class WarehouseMapper {

    public static WarehouseJpaEntity toJpaEntity(Warehouse warehouse) {
        var entity = new WarehouseJpaEntity();
        entity.setId(warehouse.getId());
        entity.setName(warehouse.getName());
        entity.setCode(warehouse.getCode());
        entity.setLocation(warehouse.getLocation());
        entity.setActive(warehouse.isActive());
        entity.setPrimary(warehouse.isPrimary());
        return entity;
    }

    public static Warehouse toDomain(WarehouseJpaEntity entity) {
        return Warehouse.rehydrate(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getLocation(),
                entity.isActive(),
                entity.isPrimary());
    }
}