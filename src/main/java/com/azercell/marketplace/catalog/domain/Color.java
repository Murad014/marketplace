package com.azercell.marketplace.catalog.domain;

import com.azercell.marketplace.catalog.domain.vo.HexCode;
import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Color {

    private final UUID id;
    private String name;
    private HexCode hexCode;
    private Status status;

    private Color(UUID id, String name, HexCode hexCode, Status status) {
        this.id = id;
        this.name = name;
        this.hexCode = hexCode;
        this.status = status;
    }

    /** Create a new, valid, ACTIVE colour. The id is generated here. */
    public static Color create(String name, HexCode hexCode) {
        validateName(name);
        validateHexCode(hexCode);
        return new Color(UUID.randomUUID(), name.trim(), hexCode, Status.ACTIVE);
    }

    /** Rebuild from storage — trusts already-persisted data, no re-validation. */
    public static Color rehydrate(UUID id, String name, HexCode hexCode, Status status) {
        validateIdIsNotNull(id);
        return new Color(id, name, hexCode, status == null ? Status.ACTIVE : status);
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    public void changeHexCode(HexCode hexCode) {
        validateHexCode(hexCode);
        this.hexCode = hexCode;
    }

    public void makeActive() {
        this.status = Status.ACTIVE;
    }

    public void makeInActive() {
        this.status = Status.IN_ACTIVE;
    }

    // <editor-fold desc="privateHelperMethods">
    private static void validateIdIsNotNull(UUID id) {
        if (id == null)
            throw new DomainException(ErrorCode.INVALID_ARGUMENT);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank())
            throw new DomainException(ErrorCode.COLOR_NAME_REQUIRED);
    }

    private static void validateHexCode(HexCode hexCode) {
        if (hexCode == null)
            throw new DomainException(ErrorCode.COLOR_HEX_INVALID);
    }
    // </editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}