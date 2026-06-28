package com.azercell.marketplace.catalog.web.controller;

import com.azercell.marketplace.catalog.application.service.BrandService;
import com.azercell.marketplace.catalog.web.dto.request.CreateBrandRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateBrandRequest;
import com.azercell.marketplace.catalog.web.dto.response.BrandResponse;
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
@RequestMapping("/api/v1/admin/brands")
public class BrandAdminController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponse>> create(@Valid @RequestBody CreateBrandRequest request) {
        return new ResponseEntity<>(ApiResponse.created(brandService.createBrand(request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> update(@PathVariable UUID id,
                                                             @Valid @RequestBody UpdateBrandRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(brandService.updateBrand(id, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(brandService.getBrandById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(brandService.getAllBrands()));
    }
}
