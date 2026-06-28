package com.azercell.marketplace.catalog.application.service.impl;

import com.azercell.marketplace.catalog.application.port.BrandRepository;
import com.azercell.marketplace.catalog.application.service.BrandService;
import com.azercell.marketplace.catalog.domain.aggregate.Brand;
import com.azercell.marketplace.catalog.web.dto.request.CreateBrandRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateBrandRequest;
import com.azercell.marketplace.catalog.web.dto.response.BrandResponse;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public BrandResponse createBrand(CreateBrandRequest request) {
        // create() normalizes + validates name/code; check uniqueness against the normalized values.
        var brand = Brand.create(request.name(), request.code());
        requireNameAvailable(brand.getName(), null);
        requireCodeAvailable(brand.getCode(), null);
        return toResponse(brandRepository.save(brand));
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(UUID id, UpdateBrandRequest request) {
        var brand = brandRepository.getBrandById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.BRAND_NOT_FOUND));

        brand.changeName(request.name());
        brand.changeCode(request.code());
        if (request.active() != null) {
            if (request.active()) brand.makeActive();
            else brand.makeInActive();
        }

        // Uniqueness, ignoring this same brand.
        requireNameAvailable(brand.getName(), id);
        requireCodeAvailable(brand.getCode(), id);

        return toResponse(brandRepository.save(brand));
    }

    @Override
    @Transactional
    public BrandResponse getBrandById(UUID id) {
        return brandRepository.getBrandById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new DomainException(ErrorCode.BRAND_NOT_FOUND));
    }

    @Override
    @Transactional
    public List<BrandResponse> getAllBrands() {
        return brandRepository.getAllBranch().stream()
                .map(this::toResponse)
                .toList();
    }

    // <editor-fold desc="privateHelperMethods">
    private void requireNameAvailable(String name, UUID selfId) {
        brandRepository.findByName(name)
                .filter(existing -> !existing.getId().equals(selfId))
                .ifPresent(existing -> { throw new DomainException(ErrorCode.BRAND_NAME_ALREADY_EXISTS); });
    }

    private void requireCodeAvailable(String code, UUID selfId) {
        brandRepository.findByCode(code)
                .filter(existing -> !existing.getId().equals(selfId))
                .ifPresent(existing -> { throw new DomainException(ErrorCode.BRAND_CODE_ALREADY_EXISTS); });
    }

    private BrandResponse toResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getCode(),
                brand.getStatus() != null ? brand.getStatus().name() : null);
    }
    // </editor-fold>
}
