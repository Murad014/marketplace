package com.azercell.marketplace.inventory.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.inventory.domain.vo.MovementType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * An immutable ledger entry recording one change to inventory (the {@code inventory_movements}
 * table). {@code change} is signed: positive adds stock, negative removes it.
 */
@Getter
public class InventoryMovement {

    private final UUID id;
    private final UUID warehouseId;
    private final UUID variantId;
    private final int change;
    private final MovementType type;
    private final String reference;   // order number, PO, transfer ref... nullable
    private final UUID performedBy;   // the acting user; nullable until the users context exists
    private final LocalDateTime createdAt;  // set by persistence auditing; null on a fresh record

    private InventoryMovement(UUID id, UUID warehouseId, UUID variantId, int change, MovementType type,
                              String reference, UUID performedBy, LocalDateTime createdAt) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.variantId = variantId;
        this.change = change;
        this.type = type;
        this.reference = reference;
        this.performedBy = performedBy;
        this.createdAt = createdAt;
    }

    public static InventoryMovement record(UUID warehouseId, UUID variantId, int change,
                                           MovementType type, String reference, UUID performedBy) {
        if (warehouseId == null || variantId == null || type == null)
            throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        if (change == 0)
            throw new DomainException(ErrorCode.INVENTORY_QUANTITY_INVALID);
        return new InventoryMovement(UUID.randomUUID(), warehouseId, variantId, change, type,
                normalizeReference(reference), performedBy, null);
    }

    public static InventoryMovement rehydrate(UUID id, UUID warehouseId, UUID variantId, int change,
                                              MovementType type, String reference, UUID performedBy,
                                              LocalDateTime createdAt) {
        return new InventoryMovement(id, warehouseId, variantId, change, type, reference, performedBy, createdAt);
    }

    private static String normalizeReference(String reference) {
        return reference == null || reference.isBlank() ? null : reference.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryMovement other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}