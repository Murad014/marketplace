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

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
public class CategoryJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "name_az", nullable = false, length = 120)
    private String nameAz;

    @Column(name = "name_en", nullable = false, length = 120)
    private String nameEn;

    @Column(nullable = false, unique = true, length = 140)
    private String slug;

    @Column(name = "description_az", columnDefinition = "text")
    private String descriptionAz;

    @Column(name = "description_en", columnDefinition = "text")
    private String descriptionEn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryJpaEntity parent;

    @OneToMany(mappedBy = "parent",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<CategoryJpaEntity> children = new ArrayList<>();


}