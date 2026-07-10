package com.azercell.marketplace.catalog.web.controller;

import com.azercell.marketplace.catalog.application.service.CategoryService;
import com.azercell.marketplace.catalog.web.controller.api.CategoryPublicApi;
import com.azercell.marketplace.catalog.web.dto.response.CategoryResponse;
import com.azercell.marketplace.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Public, storefront-facing category listing. Only active categories are exposed, localized via the
 * request's Accept-Language (resolved into {@link LocaleContextHolder}). Admin authoring lives under
 * {@link CategoryAdminController}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController implements CategoryPublicApi {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> list() {
        var categories = categoryService.getPublishedCategories(LocaleContextHolder.getLocale());
        return ResponseEntity.ok(ApiResponse.ok(categories));
    }
}
