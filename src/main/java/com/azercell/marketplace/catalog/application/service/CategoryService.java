package com.azercell.marketplace.catalog.application.service;

import com.azercell.marketplace.catalog.web.dto.request.CreateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.response.CategoryAdminResponse;
import com.azercell.marketplace.catalog.web.dto.response.CategoryResponse;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface CategoryService {

    CategoryAdminResponse createCategory(CreateCategoryRequest request);

    CategoryAdminResponse updateCategory(UUID id, UpdateCategoryRequest request);

    CategoryAdminResponse getCategoryById(UUID id);

    List<CategoryAdminResponse> getAllCategories();

    /** Public storefront read: active categories only, each localized to {@code locale} (az → AZ, else EN). */
    List<CategoryResponse> getPublishedCategories(Locale locale);
}
