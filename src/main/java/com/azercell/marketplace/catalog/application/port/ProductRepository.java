package com.azercell.marketplace.catalog.application.port;

import com.azercell.marketplace.catalog.domain.aggregate.Brand;
import com.azercell.marketplace.catalog.domain.aggregate.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    UUID insert(Product product, Brand brand);
    void update(Product product, Brand brand);
    Optional<Product> findById(UUID id);
    boolean existsBySku(String sku);

    void deleteById(UUID id);
}
