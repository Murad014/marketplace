package com.azercell.marketplace.catalog.infrastructure.persistence.mapper;

import com.azercell.marketplace.catalog.domain.ProductImage;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.ProductImageJpaEntity;

import java.util.List;

public class ProductImageMapper {
    public static ProductImageJpaEntity toJpaEntity(ProductImage productImage) {
        var productImageJpaEntity = new ProductImageJpaEntity();
        productImageJpaEntity.setId(productImage.getId());
        productImageJpaEntity.setName(productImage.getName());
        productImageJpaEntity.setUrl(productImage.getUrl());
        productImageJpaEntity.setAltText(productImage.getAltText());
        productImageJpaEntity.setPrimary(productImage.isPrimary());
        productImageJpaEntity.setSortOrder(productImage.getSortOrder());
        productImageJpaEntity.setStatus(productImage.getStatus());

        return productImageJpaEntity;
    }


    public static List<ProductImageJpaEntity> toJpaEntityList(List<ProductImage> productImageList) {
        if (productImageList == null) return List.of();
        return productImageList.stream()
                .map(ProductImageMapper::toJpaEntity)
                .toList();
    }

    public static ProductImage toDomain(ProductImageJpaEntity productImageJpaEntity){
        ProductImage productImage;

        if(productImageJpaEntity.getId() == null){
            productImage = ProductImage.create(
                    productImageJpaEntity.getUrl(),
                    productImageJpaEntity.getName(),
                    productImageJpaEntity.getAltText(),
                    productImageJpaEntity.isPrimary(),
                    productImageJpaEntity.getSortOrder()
            );
        }else{
            productImage = ProductImage.update(
                    productImageJpaEntity.getId(),
                    productImageJpaEntity.getUrl(),
                    productImageJpaEntity.getName(),
                    productImageJpaEntity.getAltText(),
                    productImageJpaEntity.isPrimary(),
                    productImageJpaEntity.getSortOrder()
            );
        }

        return productImage;
    }

    public static List<ProductImage > toDomainList(List<ProductImageJpaEntity> productImageJpaEntities) {
        if (productImageJpaEntities == null) return List.of();
        return productImageJpaEntities.stream()
                .map(ProductImageMapper::toDomain)
                .toList();
    }

}
