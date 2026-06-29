package com.azercell.marketplace.catalog.web.controller;

import com.azercell.marketplace.catalog.application.port.ProductFilter;
import com.azercell.marketplace.catalog.application.service.ProductService;
import com.azercell.marketplace.catalog.web.dto.response.ProductResponse;
import com.azercell.marketplace.catalog.web.dto.response.ProductSummaryResponse;
import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Public, employee-facing storefront endpoints. Only ACTIVE products are exposed here;
 * record-lifecycle and editing live under the admin controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController implements ProductApi {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductSummaryResponse>>> listProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var filter = new ProductFilter(name, categoryId, minPrice, maxPrice);
        var products = productService.listActiveProducts(filter, page, size);
        return ResponseEntity.ok(ApiResponse.ok(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable UUID id) {
        var product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.ok(product));
    }
}