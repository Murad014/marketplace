package com.azercell.marketplace.inventory.infrastructure.persistence.entity;

import com.azercell.marketplace.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stocks")
public class WarehouseJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(length = 200)
    private String location;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "is_primary", nullable = false)
    private boolean primary;
}