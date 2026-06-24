package com.azercell.marketplace.catalog.domain;

import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductImage {
    private final UUID id;
    private String name;
    private String url;
    private String altText;
    private boolean isPrimary;
    private int sortOrder;
    private Status status;

    private ProductImage(UUID id, String name, String url, String altText, boolean isPrimary, int sortOrder) {
        validateIdIsNotNull(id);
        validateNotBlankUrl(url);
        validateSortOrder(sortOrder);
        validateName(name);

        this.name = name;
        this.id = id;
        this.url = url;
        this.altText = altText;
        this.isPrimary = isPrimary;
        this.sortOrder = sortOrder;
    }

    public static ProductImage create(String url, String name, String altText, boolean isPrimary, int sortOrder) {
        return new ProductImage(UUID.randomUUID(), name, url, altText, isPrimary, sortOrder);
    }

    public static ProductImage update(UUID id, String url, String name, String altText, boolean isPrimary, int sortOrder) {
        return new ProductImage(id, name, url, altText, isPrimary, sortOrder);
    }

    public void makeAsPrimary() {
        this.isPrimary = true;
    }

    public void unmakeAsPrimary() {
        this.isPrimary = false;
    }

    public void reorder(int newOrder) {
        validateSortOrder(newOrder);
        this.sortOrder = newOrder;
    }

    public void setAltText(String altText){
        this.altText = altText != null ? altText.trim() : "";
    }

    public void setUrl(String url){
        validateNotBlankUrl(url);
        this.url = url;
    }

    public void changeName(String name){
        validateName(name);
        this.name = name;
    }


    public void makeActive(){
        this.status = Status.ACTIVE;
    }

    public void makeInActive(){
        this.status = Status.IN_ACTIVE;
    }

    // <editor-fold desc = "privateHelperMethods">
    void validateSortOrder(int sortOrder) {
        if (sortOrder < 0)
            throw new DomainException(ErrorCode.PRODUCT_VARIANT_IMAGE);
    }

    void validateNotBlankUrl(String url) {
        if (url == null || url.isBlank())
            throw new DomainException(ErrorCode.PRODUCT_IMAGE_URL_REQUIRED);
    }

    void validateIdIsNotNull(UUID uuid) {
        if (uuid == null)
            throw new DomainException(ErrorCode.INVALID_ARGUMENT);
    }

    void validateName(String name){
        if(name == null || name.isEmpty())
            throw new DomainException(ErrorCode.PRODUCT_NAME_REQUIRED);
    }
    // </editor-fold>
}
