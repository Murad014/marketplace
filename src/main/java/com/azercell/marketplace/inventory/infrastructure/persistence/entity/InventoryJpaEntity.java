package com.azercell.marketplace.inventory.infrastructure.persistence.entity;

import com.azercell.marketplace.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventory",
        uniqueConstraints = @UniqueConstraint(columnNames = {"stock_id", "variant_id"}))
public class InventoryJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "stock_id", nullable = false)
    private UUID stockId;

    @Column(name = "variant_id", nullable = false)
    private UUID variantId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @Column(name = "seller_price", precision = 12, scale = 2)
    private BigDecimal sellerPrice;

    @Column(name = "purchase_price", precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "low_stock_threshold", nullable = false)
    private int lowStockThreshold;
}