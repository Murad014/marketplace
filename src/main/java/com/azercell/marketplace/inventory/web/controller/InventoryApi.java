package com.azercell.marketplace.inventory.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.inventory.web.dto.request.AdjustStockRequest;
import com.azercell.marketplace.inventory.web.dto.request.RestockRequest;
import com.azercell.marketplace.inventory.web.dto.response.InventoryMovementResponse;
import com.azercell.marketplace.inventory.web.dto.response.InventoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Inventory (Admin)", description = "Stock levels, restock/adjust operations and the movement ledger.")
public interface InventoryApi {

    @Operation(summary = "Get stock by variant",
            description = "Inventory rows for a variant across all warehouses.")
    ResponseEntity<ApiResponse<List<InventoryResponse>>> getByVariant(
            @Parameter(description = "Variant id") UUID variantId);

    @Operation(summary = "Restock (+amount)",
            description = "Increases stock and records a RESTOCK movement.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restocked"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error or inventory not found")
    })
    ResponseEntity<ApiResponse<InventoryResponse>> restock(RestockRequest request);

    @Operation(summary = "Adjust (set absolute quantity)",
            description = "Sets the quantity to an absolute value and records a CORRECTION movement for the delta.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Adjusted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error or inventory not found")
    })
    ResponseEntity<ApiResponse<InventoryResponse>> adjust(AdjustStockRequest request);

    @Operation(summary = "Movement ledger for a variant", description = "Newest first; paginated.")
    ResponseEntity<ApiResponse<PageResponse<InventoryMovementResponse>>> getMovements(
            @Parameter(description = "Variant id") UUID variantId,
            @Parameter(description = "Zero-based page index", example = "0") int page,
            @Parameter(description = "Page size", example = "20") int size);
}
