package com.azercell.marketplace.catalog.web.controller;

import com.azercell.marketplace.catalog.web.dto.request.CreateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.response.CategoryResponse;
import com.azercell.marketplace.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Categories (Admin)", description = "Hierarchical product categories (optional parent, unique slug).")
public interface CategoryApi {

    @Operation(summary = "Create a category",
            description = "Creates a category. `slug` is lowercased and unique. Omit `parentId` for a root category.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Category created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation error, duplicate slug (CATEGORY_SLUG_ALREADY_EXISTS), or parent not found (CATEGORY_PARENT_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<CategoryResponse>> create(CreateCategoryRequest request);

    @Operation(summary = "Update a category",
            description = "Full update. `parentId` null makes it a root; `active` is optional. "
                    + "Re-parenting is guarded against self-parent (CATEGORY_SELF_PARENT) and cycles (CATEGORY_CIRCULAR_REFERENCE).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation/duplicate error, not found, self-parent, parent not found, or circular reference")
    })
    ResponseEntity<ApiResponse<CategoryResponse>> update(
            @Parameter(description = "Category id") UUID id,
            UpdateCategoryRequest request);

    @Operation(summary = "Get a category by id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Category not found (CATEGORY_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<CategoryResponse>> getById(
            @Parameter(description = "Category id") UUID id);

    @Operation(summary = "List all categories")
    ResponseEntity<ApiResponse<List<CategoryResponse>>> list();
}
