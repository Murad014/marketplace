package com.azercell.marketplace.catalog.infrastructure.persistence.entity;


import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_variants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "color_id"}))
@Getter
@Setter
@NoArgsConstructor
public class ProductVariantJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    private String sku;

    @Column(name = "color_id", nullable = false)
    private UUID colorId;

    @Column(name = "price_override", precision = 12, scale = 2)
    private BigDecimal priceOverride;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @ColumnTransformer(write = "?::jsonb")
    @Column(columnDefinition = "jsonb")
    private String specs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity product;

    @OneToMany(mappedBy = "variant",
            cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductImageJpaEntity> images = new ArrayList<>();

}