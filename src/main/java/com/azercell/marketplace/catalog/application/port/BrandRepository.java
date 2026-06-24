package com.azercell.marketplace.catalog.application.port;

import com.azercell.marketplace.catalog.domain.aggregate.Brand;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandRepository {
    List<Brand> getAllBranch();
    Optional<Brand> getBrandById(UUID id);
}
