package com.azercell.marketplace.catalog.web.controller;

import com.azercell.marketplace.catalog.application.service.CategoryService;
import com.azercell.marketplace.catalog.web.controller.api.CategoryApi;
import com.azercell.marketplace.catalog.web.dto.request.CreateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.response.CategoryResponse;
import com.azercell.marketplace.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
public class CategoryAdminController implements CategoryApi {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CreateCategoryRequest request) {
        return new ResponseEntity<>(ApiResponse.created(categoryService.createCategory(request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.updateCategory(id, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.getCategoryById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.getAllCategories()));
    }
}
