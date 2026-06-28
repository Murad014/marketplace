package com.azercell.marketplace.catalog.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.catalog.application.port.CategoryRepository;
import com.azercell.marketplace.catalog.domain.aggregate.Category;
import com.azercell.marketplace.catalog.infrastructure.persistence.mapper.CategoryMapper;
import com.azercell.marketplace.catalog.infrastructure.persistence.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryJpaRepositoryAdapter implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public boolean existsById(UUID id) {
        return id != null && categoryJpaRepository.existsById(id);
    }

    @Override
    public Optional<Category> getCategoryById(UUID id) {
        return categoryJpaRepository.findById(id).flatMap(CategoryMapper::toDomain);
    }

    @Override
    public List<Category> getAllCategories() {
        return CategoryMapper.toDomainList(categoryJpaRepository.findAll());
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return categoryJpaRepository.findBySlugIgnoreCase(slug).flatMap(CategoryMapper::toDomain);
    }

    @Override
    public Category save(Category category, UUID parentId) {
        // Map scalars to a fresh entity, then wire the parent FK via a managed reference (no full load).
        // createdDate/createdBy are @Column(updatable = false), so a merge by id keeps the audit fields.
        var entity = CategoryMapper.toJpaEntity(category);
        entity.setParent(parentId != null ? categoryJpaRepository.getReferenceById(parentId) : null);
        var saved = categoryJpaRepository.save(entity);
        return CategoryMapper.toDomain(saved).orElseThrow();
    }
}
