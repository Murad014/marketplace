package com.azercell.marketplace.catalog.infrastructure.persistence.mapper;

import com.azercell.marketplace.catalog.domain.aggregate.Category;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.CategoryJpaEntity;

import java.util.List;
import java.util.Optional;

public class CategoryMapper {

    public static Optional<Category> toDomain(CategoryJpaEntity entity) {
        if (entity == null) return Optional.empty();
        return Optional.of(Category.rehydrate(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getParent() != null ? entity.getParent().getId() : null));
    }

    public static List<Category> toDomainList(List<CategoryJpaEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(e -> Category.rehydrate(e.getId(), e.getName(), e.getSlug(), e.getDescription(),
                        e.getStatus(), e.getParent() != null ? e.getParent().getId() : null))
                .toList();
    }

    /** Maps scalar fields only; the parent reference is wired by the adapter (it owns the EntityManager). */
    public static CategoryJpaEntity toJpaEntity(Category category) {
        var entity = new CategoryJpaEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        entity.setSlug(category.getSlug());
        entity.setDescription(category.getDescription());
        entity.setStatus(category.getStatus());
        return entity;
    }
}
