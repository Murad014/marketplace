package com.azercell.marketplace.catalog.web.controller;

import com.azercell.marketplace.catalog.web.dto.response.ProductResponse;
import com.azercell.marketplace.catalog.web.dto.response.ProductSummaryResponse;
import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Products (Storefront)", description = "Public, employee-facing product browsing. Only ACTIVE products are exposed.")
public interface ProductApi {

    @Operation(summary = "List products (paginated)",
            description = "Returns active products. Each card includes a `monthlyFrom`/`maxMonths` installment hint.")
    ResponseEntity<ApiResponse<PageResponse<ProductSummaryResponse>>> listProducts(
            @Parameter(description = "Zero-based page index", example = "0") int page,
            @Parameter(description = "Page size (1–100)", example = "20") int size);

    @Operation(summary = "Get a product by id",
            description = "Full product detail incl. variants, colours, images and computed `installmentOptions`.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Product not found (PRODUCT_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            @Parameter(description = "Product id") UUID id);
}
