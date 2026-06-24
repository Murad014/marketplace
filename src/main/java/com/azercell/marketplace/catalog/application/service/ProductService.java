package com.azercell.marketplace.catalog.application.service;

import com.azercell.marketplace.catalog.web.dto.request.AddProductRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateProductRequest;
import com.azercell.marketplace.catalog.web.dto.response.ProductResponse;
import com.azercell.marketplace.catalog.web.dto.response.ProductSummaryResponse;
import com.azercell.marketplace.common.dto.PageResponse;

import java.util.UUID;

public interface ProductService {
    UUID addProduct(AddProductRequest request);
    void updateProduct(UUID productId, UpdateProductRequest request);
    ProductResponse getProductById(UUID productId);
    PageResponse<ProductSummaryResponse> listActiveProducts(int page, int size);
}
