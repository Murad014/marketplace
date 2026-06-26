package com.azercell.marketplace.inventory.infrastructure.persistence.entity;

import com.azercell.marketplace.common.entity.BaseEntity;
import com.azercell.marketplace.inventory.domain.vo.MovementType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventory_movements", indexes = {
        @Index(name = "idx_movement_stock_variant", columnList = "stock_id, variant_id"),
        @Index(name = "idx_movement_type", columnList = "type"),
        @Index(name = "idx_movement_created", columnList = "created_date")
})
public class InventoryMovementJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "stock_id", nullable = false)
    private UUID stockId;

    @Column(name = "variant_id", nullable = false)
    private UUID variantId;

    @Column(name = "change_amount", nullable = false)
    private int change;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MovementType type;

    @Column(length = 100)
    private String reference;

    // The user who performed the action (users context not built yet -> nullable).
    // Distinct from BaseEntity's audit createdBy (the auditor username).
    @Column(name = "performed_by")
    private UUID performedBy;
}