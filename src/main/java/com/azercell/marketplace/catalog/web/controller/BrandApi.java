package com.azercell.marketplace.catalog.web.controller;

import com.azercell.marketplace.catalog.web.dto.request.CreateBrandRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateBrandRequest;
import com.azercell.marketplace.catalog.web.dto.response.BrandResponse;
import com.azercell.marketplace.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * OpenAPI documentation for the admin brand endpoints. Kept separate from the controller so the
 * controller carries only routing/logic; springdoc merges these annotations by method signature....
 */
@Tag(name = "Brands (Admin)", description = "Create, update and read product brands (codes are uppercased and unique).")
public interface BrandApi {

    @Operation(summary = "Create a brand",
            description = "Creates a brand. `code` is normalised to UPPERCASE and must be unique (case-insensitive), as must `name`.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Brand created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation error, invalid code format, or duplicate name/code")
    })
    ResponseEntity<ApiResponse<BrandResponse>> create(CreateBrandRequest request);

    @Operation(summary = "Update a brand",
            description = "Updates name/code and optionally the active status. Uniqueness is enforced excluding this brand.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Brand updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation/duplicate error, or brand not found (BRAND_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<BrandResponse>> update(
            @Parameter(description = "Brand id", example = "b9ab895e-90d4-4353-a18b-21ab1ad6164d") UUID id,
            UpdateBrandRequest request);

    @Operation(summary = "Get a brand by id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Brand found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Brand not found (BRAND_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<BrandResponse>> getById(
            @Parameter(description = "Brand id") UUID id);

    @Operation(summary = "List all brands")
    ResponseEntity<ApiResponse<List<BrandResponse>>> list();
}
