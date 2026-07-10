package com.azercell.marketplace.catalog.infrastructure.persistence.mapper;

import com.azercell.marketplace.catalog.domain.aggregate.Category;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.CategoryJpaEntity;

import java.util.List;
import java.util.Optional;

public class CategoryMapper {

    public static Optional<Category> toDomain(CategoryJpaEntity entity) {
        if (entity == null) return Optional.empty();
        return Optional.of(rehydrate(entity));
    }

    public static List<Category> toDomainList(List<CategoryJpaEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(CategoryMapper::rehydrate).toList();
    }

    /** Maps scalar fields only; the parent reference is wired by the adapter (it owns the EntityManager). */
    public static CategoryJpaEntity toJpaEntity(Category category) {
        var entity = new CategoryJpaEntity();
        entity.setId(category.getId());
        entity.setNameAz(category.getNameAz());
        entity.setNameEn(category.getNameEn());
        entity.setSlug(category.getSlug());
        entity.setDescriptionAz(category.getDescriptionAz());
        entity.setDescriptionEn(category.getDescriptionEn());
        entity.setStatus(category.getStatus());
        return entity;
    }

    private static Category rehydrate(CategoryJpaEntity e) {
        return Category.rehydrate(
                e.getId(),
                e.getNameAz(),
                e.getNameEn(),
                e.getSlug(),
                e.getDescriptionAz(),
                e.getDescriptionEn(),
                e.getStatus(),
                e.getParent() != null ? e.getParent().getId() : null);
    }
}
