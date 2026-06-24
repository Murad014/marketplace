package com.azercell.marketplace.catalog.application.port;

import com.azercell.marketplace.catalog.domain.aggregate.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    boolean existsById(UUID id);

    Optional<Category> getCategoryById(UUID id);
}