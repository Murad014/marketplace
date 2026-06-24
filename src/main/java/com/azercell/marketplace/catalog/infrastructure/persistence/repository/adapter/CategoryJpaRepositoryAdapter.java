package com.azercell.marketplace.catalog.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.catalog.application.port.CategoryRepository;
import com.azercell.marketplace.catalog.domain.aggregate.Category;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.CategoryJpaEntity;
import com.azercell.marketplace.catalog.infrastructure.persistence.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryJpaRepositoryAdapter implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public boolean existsById(UUID id) {
        return false;
    }

    @Override
    public Optional<Category> getCategoryById(UUID id) {
        var categoryEntity = categoryJpaRepository.findById(id);
        return categoryEntity.map(this::toDomain);
    }

    //<editor-fold desc="privateHelperMethods">
    private Category toDomain(CategoryJpaEntity entity) {
        if (entity == null) return null;

        Category category = new Category(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                entity.getStatus(),
                null,
                null
        );

        if (entity.getChildren() != null) {
            for (CategoryJpaEntity child : entity.getChildren()) {
                category.addChild(toDomain(child));
            }
        }

        return category;
    }
    // </editor-fold>
}
