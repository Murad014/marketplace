package com.azercell.marketplace.inventory.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.inventory.application.service.InventoryService;
import com.azercell.marketplace.inventory.web.dto.response.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/inventory")
public class InventoryAdminController {

    private final InventoryService inventoryService;

    /** Stock rows for a variant across all warehouses. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getByVariant(
            @RequestParam UUID variantId) {
        return ResponseEntity.ok(ApiResponse.ok(inventoryService.getByVariant(variantId)));
    }
}