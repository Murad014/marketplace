package com.azercell.marketplace.catalog.infrastructure.persistence.mapper;

import com.azercell.marketplace.catalog.domain.Color;
import com.azercell.marketplace.catalog.domain.ProductImage;
import com.azercell.marketplace.catalog.domain.ProductVariant;
import com.azercell.marketplace.catalog.domain.vo.Specs;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.ProductVariantJpaEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductVariantMapper {
    public static ProductVariantJpaEntity toJpaEntity(ProductVariant productVariant) {
        var productVariantJpaEntity = new ProductVariantJpaEntity();
        productVariantJpaEntity.setId(productVariant.getId());
        productVariantJpaEntity.setSku(productVariant.getSku());
        productVariantJpaEntity.setColorId(productVariant.getColor().getId());
        if (productVariant.getPriceOverride() != null)
            productVariantJpaEntity.setPriceOverride(productVariant.getPriceOverride().amount());
        productVariantJpaEntity.setStatus(productVariant.getStatus());
        productVariantJpaEntity.setImages(ProductImageMapper.toJpaEntityList(productVariant.getProductImages()));
        productVariantJpaEntity.setId(productVariant.getId());

        var images = ProductImageMapper.toJpaEntityList(productVariant.getProductImages());
        images.forEach(img -> img.setVariant(productVariantJpaEntity));
        productVariantJpaEntity.setImages(images);

        return productVariantJpaEntity;
    }

    public static List<ProductVariantJpaEntity> toJpaEntityList(List<ProductVariant> productVariantList) {
        if (productVariantList == null) return List.of();
        return productVariantList.stream()
                .map(ProductVariantMapper::toJpaEntity)
                .toList();
    }
}
