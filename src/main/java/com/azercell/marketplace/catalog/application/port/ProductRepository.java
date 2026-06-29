package com.azercell.marketplace.catalog.application.port;

import com.azercell.marketplace.catalog.domain.aggregate.Brand;
import com.azercell.marketplace.catalog.domain.aggregate.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    UUID insert(Product product, Brand brand);
    void update(Product product, Brand brand);
    Optional<Product> findById(UUID id);
    Optional<Product> findByVariantId(UUID variantId);
    Page<Product> findActive(ProductFilter filter, Pageable pageable);
    boolean existsBySku(String sku);

    void deleteById(UUID id);
}
