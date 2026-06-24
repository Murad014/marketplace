package com.azercell.marketplace.catalog.infrastructure.persistence.mapper;

import com.azercell.marketplace.catalog.domain.aggregate.Brand;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.BrandJpaEntity;

import java.util.List;
import java.util.Optional;


public class BrandMapper {
    public static Optional<Brand> toDomain(BrandJpaEntity entity){
        if(entity == null)
            return Optional.empty();

        return Optional.of(Brand.rehydrate(entity.getId(), entity.getName(),
                entity.getCode(), entity.getStatus()));
    }

    public static List<Brand> toDomainList(List<BrandJpaEntity> jpaEntities) {
        if (jpaEntities == null) return List.of();
        return jpaEntities.stream()
                .map(e -> Brand.rehydrate(e.getId(), e.getName(), e.getCode(), e.getStatus()))
                .toList();
    }

    public static BrandJpaEntity toJpaEntity(Brand brand){
        var brandJpaEntity = new BrandJpaEntity();
        brandJpaEntity.setId(brand.getId());
        brandJpaEntity.setName(brand.getName());
        brandJpaEntity.setCode(brand.getCode());
        brandJpaEntity.setStatus(brand.getStatus());

        return brandJpaEntity;
    }
}
