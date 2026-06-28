package com.azercell.marketplace.catalog.application.service;

import com.azercell.marketplace.catalog.web.dto.request.CreateBrandRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateBrandRequest;
import com.azercell.marketplace.catalog.web.dto.response.BrandResponse;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    BrandResponse createBrand(CreateBrandRequest request);
    BrandResponse updateBrand(UUID id, UpdateBrandRequest request);
    BrandResponse getBrandById(UUID id);
    List<BrandResponse> getAllBrands();
}
