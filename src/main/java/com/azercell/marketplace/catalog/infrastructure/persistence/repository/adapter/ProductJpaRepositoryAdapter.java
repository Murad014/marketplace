package com.azercell.marketplace.catalog.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.catalog.application.port.ProductRepository;
import com.azercell.marketplace.catalog.domain.aggregate.Brand;
import com.azercell.marketplace.catalog.domain.aggregate.Product;
import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.catalog.infrastructure.persistence.mapper.ProductMapper;
import com.azercell.marketplace.catalog.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductJpaRepositoryAdapter implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public UUID insert(Product product, Brand brand) {
        var newProductJpaEntity = ProductMapper.toJpaEntity(product, brand);
       var saveResponse = productJpaRepository.save(newProductJpaEntity);

        return saveResponse.getId();
    }

    @Override
    public void update(Product product, Brand brand) {
        var newProductJpaEntity = ProductMapper.toJpaEntity(product, brand);
        productJpaRepository.save(newProductJpaEntity);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return productJpaRepository.findById(id)
                .map(ProductMapper::toDomain);
    }

    @Override
    public Optional<Product> findByVariantId(UUID variantId) {
        return productJpaRepository.findByVariantId(variantId).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findActive(Pageable pageable) {
        return productJpaRepository.findByStatus(Status.ACTIVE, pageable)
                .map(ProductMapper::toDomain);
    }

    @Override
    public boolean existsBySku(String sku) {
        return productJpaRepository.existsBySku(sku);
    }

    @Override
    public void deleteById(UUID id) {
        productJpaRepository.deleteById(id);
    }

    // <editor-fold desc="helperPrivateMethods">

    // </editor-fold>
}
