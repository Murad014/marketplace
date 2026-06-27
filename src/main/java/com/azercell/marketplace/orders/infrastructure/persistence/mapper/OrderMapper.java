package com.azercell.marketplace.orders.infrastructure.persistence.mapper;

import com.azercell.marketplace.orders.domain.OrderItem;
import com.azercell.marketplace.orders.domain.aggregate.Order;
import com.azercell.marketplace.orders.infrastructure.persistence.entity.OrderItemJpaEntity;
import com.azercell.marketplace.orders.infrastructure.persistence.entity.OrderJpaEntity;

public class OrderMapper {

    public static OrderJpaEntity toJpaEntity(Order order) {
        var entity = new OrderJpaEntity();
        entity.setId(order.getId());
        entity.setOrderNumber(order.getOrderNumber());
        entity.setUserId(order.getUserId());
        entity.setWarehouseId(order.getWarehouseId());
        entity.setStatus(order.getStatus());
        entity.setSubtotalAmount(order.getSubtotalAmount());
        entity.setDiscountAmount(order.getDiscountAmount());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setCurrency(order.getCurrency());
        entity.setPlacedAt(order.getPlacedAt());

        var items = order.getItems().stream().map(OrderMapper::toItemEntity).toList();
        items.forEach(i -> i.setOrder(entity));
        entity.setItems(new java.util.ArrayList<>(items));
        return entity;
    }

    public static Order toDomain(OrderJpaEntity entity) {
        var items = entity.getItems().stream().map(OrderMapper::toItemDomain).toList();
        return Order.rehydrate(
                entity.getId(),
                entity.getOrderNumber(),
                entity.getUserId(),
                entity.getWarehouseId(),
                entity.getStatus(),
                items,
                entity.getSubtotalAmount(),
                entity.getDiscountAmount(),
                entity.getTotalAmount(),
                entity.getCurrency(),
                entity.getPlacedAt());
    }

    private static OrderItemJpaEntity toItemEntity(OrderItem item) {
        var e = new OrderItemJpaEntity();
        e.setId(item.getId());
        e.setVariantId(item.getVariantId());
        e.setProductName(item.getProductName());
        e.setProductSku(item.getProductSku());
        e.setColorName(item.getColorName());
        e.setOriginalPrice(item.getOriginalPrice());
        e.setUnitPrice(item.getUnitPrice());
        e.setQuantity(item.getQuantity());
        e.setLineTotal(item.getLineTotal());
        e.setWasPromo(item.isWasPromo());
        e.setPromoLabel(item.getPromoLabel());
        return e;
    }

    private static OrderItem toItemDomain(OrderItemJpaEntity e) {
        return OrderItem.rehydrate(
                e.getId(),
                e.getVariantId(),
                e.getProductName(),
                e.getProductSku(),
                e.getColorName(),
                e.getOriginalPrice(),
                e.getUnitPrice(),
                e.getQuantity(),
                e.getLineTotal(),
                e.isWasPromo(),
                e.getPromoLabel());
    }
}
