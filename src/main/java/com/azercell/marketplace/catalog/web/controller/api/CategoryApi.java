package com.azercell.marketplace.catalog.web.controller.api;

import com.azercell.marketplace.catalog.web.dto.request.CreateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateCategoryRequest;
import com.azercell.marketplace.catalog.web.dto.response.CategoryAdminResponse;
import com.azercell.marketplace.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Categories (Admin)", description = "Hierarchical, bilingual product categories (AZ + EN names; optional parent; unique slug).")
public interface CategoryApi {

    @Operation(summary = "Create a category",
            description = "Creates a bilingual category (nameAz + nameEn). `slug` is lowercased, unique and language-neutral. Omit `parentId` for a root category.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Category created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation error, duplicate slug (CATEGORY_SLUG_ALREADY_EXISTS), or parent not found (CATEGORY_PARENT_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<CategoryAdminResponse>> create(CreateCategoryRequest request);

    @Operation(summary = "Update a category",
            description = "Full update (both languages). `parentId` null makes it a root; `active` is optional. "
                    + "Re-parenting is guarded against self-parent (CATEGORY_SELF_PARENT) and cycles (CATEGORY_CIRCULAR_REFERENCE).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation/duplicate error, not found, self-parent, parent not found, or circular reference")
    })
    ResponseEntity<ApiResponse<CategoryAdminResponse>> update(
            @Parameter(description = "Category id") UUID id,
            UpdateCategoryRequest request);

    @Operation(summary = "Get a category by id (both languages)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Category not found (CATEGORY_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<CategoryAdminResponse>> getById(
            @Parameter(description = "Category id") UUID id);

    @Operation(summary = "List all categories (both languages, active + inactive)")
    ResponseEntity<ApiResponse<List<CategoryAdminResponse>>> list();
}
