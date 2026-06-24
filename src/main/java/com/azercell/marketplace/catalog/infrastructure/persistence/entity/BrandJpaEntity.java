package com.azercell.marketplace.catalog.infrastructure.persistence.entity;

import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="brand")
public class BrandJpaEntity extends BaseEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length=140)
    private String name;

    @Column(name="code", unique = true, nullable = false)
    private String code;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<ProductJpaEntity> products = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;
}