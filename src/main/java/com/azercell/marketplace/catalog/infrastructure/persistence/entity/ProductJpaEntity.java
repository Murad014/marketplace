package com.azercell.marketplace.catalog.infrastructure.persistence.entity;

import com.azercell.marketplace.catalog.domain.vo.Availability;
import com.azercell.marketplace.catalog.domain.vo.Currency;
import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.entity.BaseEntity;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class ProductJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    private String sku;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name="promo_price", precision = 12, scale = 2)
    private BigDecimal promoPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandJpaEntity brand;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "base_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_currency", nullable = false, length = 5)
    private Currency priceCurrency;

    @ColumnTransformer(write = "?::jsonb")
    @Column(columnDefinition = "jsonb")
    private String specs;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Availability availability;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductVariantJpaEntity> variants = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_credit_plans",
            joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "credit_plan_id")
    private Set<UUID> creditPlanIds = new HashSet<>();
}