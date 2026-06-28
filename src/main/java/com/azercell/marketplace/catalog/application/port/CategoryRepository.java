package com.azercell.marketplace.catalog.application.port;

import com.azercell.marketplace.catalog.domain.aggregate.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    boolean existsById(UUID id);

    Optional<Category> getCategoryById(UUID id);

    List<Category> getAllCategories();

    Optional<Category> findBySlug(String slug);

    /** Persists the category, wiring {@code parentId} (null = root) to the parent FK. */
    Category save(Category category, UUID parentId);
}