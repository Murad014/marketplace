package com.azercell.marketplace.catalog.infrastructure.persistence.mapper;

import com.azercell.marketplace.catalog.domain.aggregate.Brand;
import com.azercell.marketplace.catalog.domain.aggregate.Product;
import com.azercell.marketplace.catalog.domain.vo.Money;
import com.azercell.marketplace.catalog.domain.vo.Specs;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.ProductJpaEntity;

public class ProductMapper {
    public static ProductJpaEntity toJpaEntity(Product product, Brand brand){
        var productJpaEntity = new ProductJpaEntity();
        productJpaEntity.setId(product.getId());
        productJpaEntity.setSku(product.getSku());
        productJpaEntity.setBrand(BrandMapper.toJpaEntity(brand));
        productJpaEntity.setName(product.getName());
        productJpaEntity.setDescription(product.getDescription());
        productJpaEntity.setBasePrice(product.getBasePrice().amount());
        product.getPromoPrice().ifPresent(m -> productJpaEntity.setPromoPrice(m.amount()));
        productJpaEntity.setPriceCurrency(product.getPriceCurrency());
        productJpaEntity.setSpecs(product.getSpecs().json());
        productJpaEntity.setCategoryId(product.getCategoryId());
        productJpaEntity.setCreditPlanIds(product.getCreditPlans());
        productJpaEntity.setAvailability(product.getAvailability());
        productJpaEntity.setStatus(product.getStatus());

        var variants = ProductVariantMapper.toJpaEntityList(product.getProductVariants());
        variants.forEach(v -> v.setProduct(productJpaEntity));
        productJpaEntity.setVariants(variants);

        return productJpaEntity;
    }

    public static Product toDomain(ProductJpaEntity entity) {
        var basePrice = Money.of(entity.getBasePrice());
        var promoPrice = entity.getPromoPrice() != null ? Money.of(entity.getPromoPrice()) : null;
        var specs = new Specs(entity.getSpecs());
        var variants = ProductVariantMapper.toDomainList(entity.getVariants());

        return new Product(
                entity.getId(),
                entity.getSku(),
                entity.getName(),
                entity.getBrand().getId(),
                entity.getDescription(),
                basePrice,
                promoPrice,
                entity.getPriceCurrency(),
                specs,
                entity.getCategoryId(),
                variants,
                entity.getCreditPlanIds(),
                entity.getAvailability(),
                entity.getStatus());
    }
}