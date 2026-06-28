package com.azercell.marketplace.catalog.application.service.impl;

import com.azercell.marketplace.catalog.application.port.CategoryRepository;
import com.azercell.marketplace.catalog.application.service.CategoryService;
import com.azercell.marketplace.catalog.domain.aggregate.Category;
import com.azercell.marketplace.catalog.web.dto.request.CreateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.response.CategoryResponse;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        // create() normalizes + validates; check slug uniqueness against the normalized value.
        var category = Category.create(request.name(), request.slug(), request.description(), request.parentId());
        requireSlugAvailable(category.getSlug(), null);
        if (request.parentId() != null) requireParentExists(request.parentId());
        return toResponse(categoryRepository.save(category, category.getParentId()));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(UUID id, UpdateCategoryRequest request) {
        var category = categoryRepository.getCategoryById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.CATEGORY_NOT_FOUND));

        category.changeName(request.name());
        category.changeSlug(request.slug());
        category.changeDescription(request.description());
        if (request.active() != null) {
            if (request.active()) category.makeActive();
            else category.makeInactive();
        }

        // Re-parent (null = make root). Guards: parent must exist, no self-reference, no deeper cycle.
        if (request.parentId() != null) {
            requireParentExists(request.parentId());
        }
        category.changeParent(request.parentId());          // rejects direct self-parent (CATEGORY_SELF_PARENT)
        if (request.parentId() != null) {
            requireNoCycle(id, request.parentId());          // rejects making it a child of its own descendant
        }

        requireSlugAvailable(category.getSlug(), id);

        return toResponse(categoryRepository.save(category, category.getParentId()));
    }

    @Override
    @Transactional
    public CategoryResponse getCategoryById(UUID id) {
        return categoryRepository.getCategoryById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new DomainException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    @Transactional
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.getAllCategories().stream()
                .map(this::toResponse)
                .toList();
    }

    // <editor-fold desc="privateHelperMethods">
    private void requireSlugAvailable(String slug, UUID selfId) {
        categoryRepository.findBySlug(slug)
                .filter(existing -> !existing.getId().equals(selfId))
                .ifPresent(existing -> { throw new DomainException(ErrorCode.CATEGORY_SLUG_ALREADY_EXISTS); });
    }

    private void requireParentExists(UUID parentId) {
        if (!categoryRepository.existsById(parentId))
            throw new DomainException(ErrorCode.CATEGORY_PARENT_NOT_FOUND);
    }

    /** Walk up from the proposed parent; if we reach {@code categoryId}, the move would create a cycle. */
    private void requireNoCycle(UUID categoryId, UUID proposedParentId) {
        UUID ancestor = proposedParentId;
        while (ancestor != null) {
            if (ancestor.equals(categoryId))
                throw new DomainException(ErrorCode.CATEGORY_CIRCULAR_REFERENCE);
            ancestor = categoryRepository.getCategoryById(ancestor)
                    .map(Category::getParentId)
                    .orElse(null);
        }
    }

    private CategoryResponse toResponse(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getSlug(),
                c.getDescription(),
                c.getStatus() != null ? c.getStatus().name() : null,
                c.getParentId());
    }
    // </editor-fold>
}
