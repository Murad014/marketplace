package com.azercell.marketplace.catalog.web.controller.api;

import com.azercell.marketplace.catalog.web.dto.request.AddProductRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateProductRequest;
import com.azercell.marketplace.catalog.web.dto.response.ProductCreatedResponse;
import com.azercell.marketplace.catalog.web.dto.response.ProductResponse;
import com.azercell.marketplace.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Locale;
import java.util.UUID;

@Tag(name = "Products (Admin)", description = "Create, update and read products (with variants, colours, images, specs and credit plans).")
public interface ProductAdminApi {

    @Operation(summary = "Create a product",
            description = "Creates a product with its variants. Entered per-variant stock is seeded into the primary "
                    + "warehouse; if stock is provided but no primary warehouse exists, the whole create is rolled back "
                    + "(INVENTORY_NO_PRIMARY_WAREHOUSE).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation error, duplicate SKU, or no primary warehouse for entered stock")
    })
    ResponseEntity<ApiResponse<ProductCreatedResponse>> addProduct(
            AddProductRequest request,
            @Parameter(hidden = true) Locale locale);

    @Operation(summary = "Get a product by id (admin)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Product not found (PRODUCT_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "Product id") UUID id);

    @Operation(summary = "Update a product",
            description = "Full update incl. variant reconciliation (add/update/remove) and promo/price handling.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error or product not found")
    })
    ResponseEntity<ApiResponse<Void>> updateProduct(
            @Parameter(description = "Product id") UUID id,
            UpdateProductRequest request,
            @Parameter(hidden = true) Locale locale);
}
