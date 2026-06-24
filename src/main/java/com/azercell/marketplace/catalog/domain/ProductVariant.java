package com.azercell.marketplace.catalog.domain;

import com.azercell.marketplace.catalog.domain.vo.Money;
import com.azercell.marketplace.catalog.domain.vo.Specs;
import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.util.*;

public class ProductVariant {
    @Getter
    private final UUID id;

    @Getter
    private final String sku;
    @Getter
    private Color color;
    @Getter
    private Money priceOverride;
    @Getter
    private Status status;
    @Getter
    private Specs specs;

    private List<ProductImage> productImages;

    private ProductVariant(UUID id, String sku, Color color, Money priceOverride, Status status,
                           Specs specs, List<ProductImage> productImages) {
        validateColorIsNotNull(color);

        this.id = id;
        this.sku = sku == null ? generateSku(color) : sku;
        this.color = color;
        this.priceOverride = priceOverride;
        this.status = status;
        this.specs = specs;

        this.productImages = new ArrayList<>();
        productImages.forEach(this::addImage);
    }

    private String generateSku(Color color) {
        return color.getName().toUpperCase() +
                "-" +
                id.toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();
    }

    public static ProductVariant create(Color color, Specs specs, List<ProductImage> images) {
        return new ProductVariant(UUID.randomUUID(), null, color, null, Status.ACTIVE, specs, images);
    }

    public static ProductVariant update(UUID id,
                                        String sku,
                                        Color color,
                                        Specs specs,
                                        Status status,
                                        List<ProductImage> images) {
        validateIdIsNotNull(id);
        return new ProductVariant(id, sku, color, null, status, specs, images);
    }

    public void makeActive() {
        this.status = Status.ACTIVE;
    }

    public void makeInActive() {
        this.status = Status.IN_ACTIVE;
    }

    public void changeColor(Color color) {
        validateColorIsNotNull(color);
        this.color = color;
    }

    public List<ProductImage> getProductImages() {
        return List.copyOf(this.productImages);
    }

    public void overridePrice(Money newPrice) {
        this.priceOverride = newPrice;
    }

    public void changeSpecs(Specs specs) {
        this.specs = specs;
    }

    public void addImage(ProductImage newImage) {
        validateImageIsNotNull(newImage);
        validateDoesNotExistsDuplicate(newImage);
        if (newImage.isPrimary())
            unMarkOthersImagesPrimary();

        if (productImages != null)
            productImages.add(newImage);
        else
            productImages = new ArrayList<>(List.of(newImage));
    }

    public void removeImage(UUID imageId) {
        this.productImages.removeIf(image -> image.getId().equals(imageId));
        guaranteeExactlyOnePrimaryImage();
    }


    public void syncImages(List<ProductImage> incoming) {
        List<ProductImage> incomingList = incoming == null ? List.of() : incoming;

        Set<UUID> incomingIds = incomingList.stream()
                .map(ProductImage::getId)
                .filter(Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());

        this.productImages.removeIf(existing -> !incomingIds.contains(existing.getId()));

        for (ProductImage in : incomingList) {
            if (in.getId() == null || this.productImages.stream().noneMatch(i -> i.getId().equals(in.getId()))) {
                addImage(in);                          // new image -> through addImage (dup-url check runs)
            } else {
                ProductImage existing = this.productImages.stream()
                        .filter(i -> i.getId().equals(in.getId())).findFirst().orElseThrow();
                existing.changeName(in.getName());
                existing.setUrl(in.getUrl());
                existing.setAltText(in.getAltText());
                existing.reorder(in.getSortOrder());
                if (in.isPrimary()) {
                    unMarkOthersImagesPrimary();
                    existing.makeAsPrimary();
                }
            }
        }

        guaranteeExactlyOnePrimaryImage();
    }


    // <editor-fold desc="privateHelperMethods">
    private void guaranteeExactlyOnePrimaryImage(){
        if (!productImages.isEmpty() && productImages.stream().noneMatch(ProductImage::isPrimary))
            productImages.get(0).makeAsPrimary();
    }
    private void unMarkOthersImagesPrimary() {
        this.productImages.forEach(ProductImage::unmakeAsPrimary);
    }


    private void validateDoesNotExistsDuplicate(ProductImage newImage) {
        if (this.productImages == null)
            return;
        var newUrl = newImage.getUrl().trim();

        var hasDuplicate = this.productImages
                .stream()
                .anyMatch(image ->
                        image.getUrl().equalsIgnoreCase(newUrl));

        if (hasDuplicate)
            throw new DomainException(ErrorCode.PRODUCT_VARIANT_IMAGE_DUPLICATE);

    }

    public void validateColorIsNotNull(Color color) {
        if (color == null)
            throw new DomainException(ErrorCode.VARIANT_COLOR_REQUIRED);
    }

    public void validateImageIsNotNull(ProductImage productImages) {
        if (productImages == null)
            throw new DomainException(ErrorCode.PRODUCT_VARIANT_IMAGE);
    }

    public static void validateIdIsNotNull(UUID id) {
        if (id == null)
            throw new DomainException(ErrorCode.INVALID_ARGUMENT);
    }
    // </editor-fold>

}
