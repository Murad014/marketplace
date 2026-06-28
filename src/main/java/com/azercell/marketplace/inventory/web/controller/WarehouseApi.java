package com.azercell.marketplace.inventory.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.inventory.web.dto.request.CreateWarehouseRequest;
import com.azercell.marketplace.inventory.web.dto.request.UpdateWarehouseRequest;
import com.azercell.marketplace.inventory.web.dto.response.WarehouseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Warehouses (Admin)", description = "Manage warehouses (stock locations). Exactly one can be primary.")
public interface WarehouseApi {

    @Operation(summary = "Create a warehouse",
            description = "Code must be 2–30 chars (letters, digits, hyphen) and unique.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Warehouse created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error or duplicate code")
    })
    ResponseEntity<ApiResponse<WarehouseResponse>> create(CreateWarehouseRequest request);

    @Operation(summary = "Update a warehouse")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Warehouse updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error or warehouse not found")
    })
    ResponseEntity<ApiResponse<WarehouseResponse>> update(
            @Parameter(description = "Warehouse id") UUID id, UpdateWarehouseRequest request);

    @Operation(summary = "Get a warehouse by id")
    ResponseEntity<ApiResponse<WarehouseResponse>> getById(@Parameter(description = "Warehouse id") UUID id);

    @Operation(summary = "List warehouses (paginated)")
    ResponseEntity<ApiResponse<PageResponse<WarehouseResponse>>> list(
            @Parameter(description = "Zero-based page index", example = "0") int page,
            @Parameter(description = "Page size", example = "20") int size);

    @Operation(summary = "Activate a warehouse")
    ResponseEntity<ApiResponse<WarehouseResponse>> activate(@Parameter(description = "Warehouse id") UUID id);

    @Operation(summary = "Deactivate a warehouse",
            description = "Deactivating the primary warehouse also clears its primary flag.")
    ResponseEntity<ApiResponse<WarehouseResponse>> deactivate(@Parameter(description = "Warehouse id") UUID id);

    @Operation(summary = "Set as primary warehouse",
            description = "Makes this the single primary warehouse (used for stock seeding and order fulfilment).")
    ResponseEntity<ApiResponse<WarehouseResponse>> setPrimary(@Parameter(description = "Warehouse id") UUID id);
}
