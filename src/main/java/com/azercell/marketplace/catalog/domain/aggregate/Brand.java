package com.azercell.marketplace.catalog.domain.aggregate;

import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Brand {

    private final UUID id;
    private String name;
    private String code;          // short SKU code, e.g. APPL, DELL, LOGI
    private Status status;

    private Brand(UUID id, String name, String code, Status status) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.status = status;
    }

    public static Brand create(String name, String code) {
        return new Brand(UUID.randomUUID(), normalizeName(name), normalizeCode(code), Status.ACTIVE);
    }


    public static Brand rehydrate(UUID id, String name, String code, Status status) {
        if (id == null) throw new DomainException(ErrorCode.BRAND_ID_REQUIRED);
        return new Brand(id, name, code, status == null ? Status.ACTIVE : status);
    }

    public void changeName(String name) {
        this.name = normalizeName(name);
    }

    public void changeCode(String code) {
        this.code = normalizeCode(code);
    }

    public void makeActive() {
        this.status = Status.ACTIVE;
    }

    public void makeInActive() {
        this.status = Status.IN_ACTIVE;
    }

    // <editor-fold desc="privateHelperMethods">
    private static String normalizeName(String name) {
        if (name == null || name.isBlank())
            throw new DomainException(ErrorCode.BRAND_NAME_REQUIRED);
        return name.trim();
    }

    private static String normalizeCode(String code) {
        if (code == null || code.isBlank())
            throw new DomainException(ErrorCode.BRAND_CODE_REQUIRED);
        String normalized = code.trim().toUpperCase();
        if (!normalized.matches("^[A-Z0-9]{2,20}$"))
            throw new DomainException(ErrorCode.BRAND_CODE_INVALID);
        return normalized;
    }
    // </editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brand other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}