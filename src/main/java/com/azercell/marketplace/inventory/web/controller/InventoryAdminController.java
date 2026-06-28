package com.azercell.marketplace.inventory.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.inventory.application.service.InventoryService;
import com.azercell.marketplace.inventory.web.dto.request.AdjustStockRequest;
import com.azercell.marketplace.inventory.web.dto.request.RestockRequest;
import com.azercell.marketplace.inventory.web.dto.response.InventoryMovementResponse;
import com.azercell.marketplace.inventory.web.dto.response.InventoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/inventory")
public class InventoryAdminController implements InventoryApi {

    private final InventoryService inventoryService;

    /** Stock rows for a variant across all warehouses. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getByVariant(
            @RequestParam UUID variantId) {
        return ResponseEntity.ok(ApiResponse.ok(inventoryService.getByVariant(variantId)));
    }

    @PostMapping("/restock")
    public ResponseEntity<ApiResponse<InventoryResponse>> restock(
            @Valid @RequestBody RestockRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(inventoryService.restock(request)));
    }

    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<InventoryResponse>> adjust(
            @Valid @RequestBody AdjustStockRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(inventoryService.adjustQuantity(request)));
    }

    /** Movement ledger for a variant, newest first. */
    @GetMapping("/movements")
    public ResponseEntity<ApiResponse<PageResponse<InventoryMovementResponse>>> getMovements(
            @RequestParam UUID variantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(inventoryService.getMovements(variantId, page, size)));
    }
}