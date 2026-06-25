package com.azercell.marketplace.inventory.infrastructure.persistence.mapper;

import com.azercell.marketplace.inventory.domain.aggregate.Inventory;
import com.azercell.marketplace.inventory.infrastructure.persistence.entity.InventoryJpaEntity;

public class InventoryMapper {

    public static InventoryJpaEntity toJpaEntity(Inventory inventory) {
        var entity = new InventoryJpaEntity();
        entity.setId(inventory.getId());
        entity.setStockId(inventory.getWarehouseId());
        entity.setVariantId(inventory.getVariantId());
        entity.setQuantity(inventory.getQuantity());
        entity.setReservedQuantity(inventory.getReservedQuantity());
        entity.setSellerPrice(inventory.getSellerPrice());
        entity.setPurchasePrice(inventory.getPurchasePrice());
        entity.setLowStockThreshold(inventory.getLowStockThreshold());
        return entity;
    }

    public static Inventory toDomain(InventoryJpaEntity entity) {
        return Inventory.rehydrate(
                entity.getId(),
                entity.getStockId(),
                entity.getVariantId(),
                entity.getQuantity(),
                entity.getReservedQuantity(),
                entity.getSellerPrice(),
                entity.getPurchasePrice(),
                entity.getLowStockThreshold());
    }
}