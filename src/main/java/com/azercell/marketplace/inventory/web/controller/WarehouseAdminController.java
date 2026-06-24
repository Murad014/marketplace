package com.azercell.marketplace.inventory.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.inventory.application.service.WarehouseService;
import com.azercell.marketplace.inventory.web.dto.request.CreateWarehouseRequest;
import com.azercell.marketplace.inventory.web.dto.request.UpdateWarehouseRequest;
import com.azercell.marketplace.inventory.web.dto.response.WarehouseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/warehouses")
public class WarehouseAdminController {

    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<ApiResponse<WarehouseResponse>> create(
            @Valid @RequestBody CreateWarehouseRequest request) {
        var created = warehouseService.create(request);
        return new ResponseEntity<>(ApiResponse.created(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateWarehouseRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(warehouseService.update(id, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(warehouseService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<WarehouseResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(warehouseService.list(page, size)));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<WarehouseResponse>> activate(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(warehouseService.activate(id)));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<WarehouseResponse>> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(warehouseService.deactivate(id)));
    }

    @PatchMapping("/{id}/primary")
    public ResponseEntity<ApiResponse<WarehouseResponse>> setPrimary(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(warehouseService.setPrimary(id)));
    }
}