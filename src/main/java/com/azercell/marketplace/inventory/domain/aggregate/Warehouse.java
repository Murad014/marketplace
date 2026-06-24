package com.azercell.marketplace.inventory.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * A stock location / warehouse (the {@code stocks} table). Inventory rows and movements
 * reference a warehouse, so this aggregate must exist before any stock can be tracked.
 */
@Getter
public class Warehouse {

    private final UUID id;
    private String name;
    private String code;          // short location code, e.g. BAKU-A
    private String location;      // address / city
    private boolean active;
    private boolean primary;      // the default target for initial stock seeding

    private Warehouse(UUID id, String name, String code, String location, boolean active, boolean primary) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.location = location;
        this.active = active;
        this.primary = primary;
    }

    public static Warehouse create(String name, String code, String location) {
        return new Warehouse(UUID.randomUUID(),
                normalizeName(name),
                normalizeCode(code),
                normalizeLocation(location),
                true,
                false);
    }

    /** Rebuild from storage — trusts already-persisted data, no re-validation. */
    public static Warehouse rehydrate(UUID id, String name, String code, String location,
                                      boolean active, boolean primary) {
        if (id == null) throw new DomainException(ErrorCode.WAREHOUSE_NOT_FOUND);
        return new Warehouse(id, name, code, location, active, primary);
    }

    public void changeName(String name) {
        this.name = normalizeName(name);
    }

    public void changeLocation(String location) {
        this.location = normalizeLocation(location);
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
        this.primary = false;   // a primary warehouse must be active; demote on deactivation
    }

    public void markAsPrimary() {
        if (!active)
            throw new DomainException(ErrorCode.WAREHOUSE_NOT_FOUND);
        this.primary = true;
    }

    public void unmarkAsPrimary() {
        this.primary = false;
    }

    // <editor-fold desc="privateHelperMethods">
    private static String normalizeName(String name) {
        if (name == null || name.isBlank())
            throw new DomainException(ErrorCode.WAREHOUSE_NAME_REQUIRED);
        return name.trim();
    }

    private static String normalizeCode(String code) {
        if (code == null || code.isBlank())
            throw new DomainException(ErrorCode.WAREHOUSE_CODE_REQUIRED);
        String normalized = code.trim().toUpperCase();
        if (!normalized.matches("^[A-Z0-9-]{2,30}$"))
            throw new DomainException(ErrorCode.WAREHOUSE_CODE_INVALID);
        return normalized;
    }

    private static String normalizeLocation(String location) {
        return location == null || location.isBlank() ? null : location.trim();
    }
    // </editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Warehouse other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}