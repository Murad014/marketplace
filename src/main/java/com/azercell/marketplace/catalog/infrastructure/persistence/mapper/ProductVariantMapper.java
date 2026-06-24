package com.azercell.marketplace.catalog.infrastructure.persistence.mapper;

import com.azercell.marketplace.catalog.domain.Color;
import com.azercell.marketplace.catalog.domain.ProductImage;
import com.azercell.marketplace.catalog.domain.ProductVariant;
import com.azercell.marketplace.catalog.domain.vo.Specs;
import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.ProductVariantJpaEntity;

import java.util.List;

public class ProductVariantMapper {
    public static ProductVariantJpaEntity toJpaEntity(ProductVariant productVariant) {
        var productVariantJpaEntity = new ProductVariantJpaEntity();
        productVariantJpaEntity.setId(productVariant.getId());
        productVariantJpaEntity.setSku(productVariant.getSku());
        productVariantJpaEntity.setColorId(productVariant.getColor().getId());
        if (productVariant.getPriceOverride() != null)
            productVariantJpaEntity.setPriceOverride(productVariant.getPriceOverride().amount());
        productVariantJpaEntity.setStatus(productVariant.getStatus());
        if (productVariant.getSpecs() != null)
            productVariantJpaEntity.setSpecs(productVariant.getSpecs().json());

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

    public static ProductVariant toDomain(ProductVariantJpaEntity entity) {
        // The variant row stores only color_id; name/hex live in the colors table and are
        // not joined here. We rehydrate a colour from the id alone (Color.rehydrate allows
        // a null name/hex). On the update path the colour is overwritten from the request.
        var color = Color.rehydrate(entity.getColorId(), null, null, Status.ACTIVE);
        var specs = new Specs(entity.getSpecs());
        var images = ProductImageMapper.toDomainList(entity.getImages());

        return ProductVariant.update(
                entity.getId(),
                entity.getSku(),
                color,
                specs,
                entity.getStatus(),
                images);
    }

    public static List<ProductVariant> toDomainList(List<ProductVariantJpaEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(ProductVariantMapper::toDomain)
                .toList();
    }
}
