package com.azercell.marketplace.catalog.application.service.impl;

import com.azercell.marketplace.catalog.application.port.CategoryRepository;
import com.azercell.marketplace.catalog.application.service.CategoryService;
import com.azercell.marketplace.catalog.domain.aggregate.Category;
import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.catalog.web.dto.request.CreateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.response.CategoryAdminResponse;
import com.azercell.marketplace.catalog.web.dto.response.CategoryResponse;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryAdminResponse createCategory(CreateCategoryRequest request) {
        // create() normalizes + validates; check slug uniqueness against the normalized value.
        var category = Category.create(request.nameAz(), request.nameEn(), request.slug(),
                request.descriptionAz(), request.descriptionEn(), request.parentId());
        requireSlugAvailable(category.getSlug(), null);
        if (request.parentId() != null) requireParentExists(request.parentId());
        return toAdminResponse(categoryRepository.save(category, category.getParentId()));
    }

    @Override
    @Transactional
    public CategoryAdminResponse updateCategory(UUID id, UpdateCategoryRequest request) {
        var category = categoryRepository.getCategoryById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.CATEGORY_NOT_FOUND));

        category.changeName(request.nameAz(), request.nameEn());
        category.changeSlug(request.slug());
        category.changeDescription(request.descriptionAz(), request.descriptionEn());
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

        return toAdminResponse(categoryRepository.save(category, category.getParentId()));
    }

    @Override
    @Transactional
    public CategoryAdminResponse getCategoryById(UUID id) {
        return categoryRepository.getCategoryById(id)
                .map(this::toAdminResponse)
                .orElseThrow(() -> new DomainException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    @Transactional
    public List<CategoryAdminResponse> getAllCategories() {
        return categoryRepository.getAllCategories().stream()
                .map(this::toAdminResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<CategoryResponse> getPublishedCategories(Locale locale) {
        boolean az = locale != null && "az".equalsIgnoreCase(locale.getLanguage());
        return categoryRepository.getAllCategories().stream()
                .filter(c -> c.getStatus() == Status.ACTIVE)
                .map(c -> toPublicResponse(c, az))
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

    private CategoryAdminResponse toAdminResponse(Category c) {
        return new CategoryAdminResponse(
                c.getId(),
                c.getNameAz(),
                c.getNameEn(),
                c.getSlug(),
                c.getDescriptionAz(),
                c.getDescriptionEn(),
                c.getStatus() != null ? c.getStatus().name() : null,
                c.getParentId());
    }

    private CategoryResponse toPublicResponse(Category c, boolean az) {
        return new CategoryResponse(
                c.getId(),
                az ? c.getNameAz() : c.getNameEn(),
                c.getSlug(),
                az ? c.getDescriptionAz() : c.getDescriptionEn(),
                c.getParentId());
    }
    // </editor-fold>
}
