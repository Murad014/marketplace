package com.azercell.marketplace.inventory.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Stock of one variant in one warehouse (the {@code inventory} table, unique per
 * (stock_id, variant_id)). {@code availableQuantity} is derived: quantity − reserved.
 */
@Getter
public class Inventory {

    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 5;

    private final UUID id;
    private final UUID warehouseId;
    private final UUID variantId;
    private int quantity;
    private int reservedQuantity;
    private BigDecimal sellerPrice;     // nullable until procurement sets it
    private BigDecimal purchasePrice;   // nullable until procurement sets it
    private int lowStockThreshold;

    private Inventory(UUID id, UUID warehouseId, UUID variantId, int quantity, int reservedQuantity,
                      BigDecimal sellerPrice, BigDecimal purchasePrice, int lowStockThreshold) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.variantId = variantId;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.sellerPrice = sellerPrice;
        this.purchasePrice = purchasePrice;
        this.lowStockThreshold = lowStockThreshold;
    }

    public static Inventory create(UUID warehouseId, UUID variantId, int quantity) {
        if (warehouseId == null || variantId == null)
            throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        if (quantity < 0)
            throw new DomainException(ErrorCode.INVENTORY_QUANTITY_INVALID);
        return new Inventory(UUID.randomUUID(), warehouseId, variantId, quantity, 0,
                null, null, DEFAULT_LOW_STOCK_THRESHOLD);
    }

    public static Inventory rehydrate(UUID id, UUID warehouseId, UUID variantId, int quantity,
                                      int reservedQuantity, BigDecimal sellerPrice, BigDecimal purchasePrice,
                                      int lowStockThreshold) {
        return new Inventory(id, warehouseId, variantId, quantity, reservedQuantity,
                sellerPrice, purchasePrice, lowStockThreshold);
    }

    /** Derived availability — never persisted as a source of truth. */
    public int availableQuantity() {
        return quantity - reservedQuantity;
    }

    public void restock(int amount) {
        requirePositive(amount);
        this.quantity += amount;
    }

    public void reserve(int amount) {
        requirePositive(amount);
        if (availableQuantity() < amount)
            throw new DomainException(ErrorCode.INVENTORY_INSUFFICIENT_STOCK);
        this.reservedQuantity += amount;
    }

    public void release(int amount) {
        requirePositive(amount);
        this.reservedQuantity = Math.max(0, this.reservedQuantity - amount);
    }

    /** Fulfil a sale: consume both reserved units and on-hand quantity. */
    public void shipReserved(int amount) {
        requirePositive(amount);
        if (reservedQuantity < amount || quantity < amount)
            throw new DomainException(ErrorCode.INVENTORY_INSUFFICIENT_STOCK);
        this.reservedQuantity -= amount;
        this.quantity -= amount;
    }

    /** Manual stock correction; can't drop below what's already reserved. */
    public void adjustQuantity(int newQuantity) {
        if (newQuantity < 0)
            throw new DomainException(ErrorCode.INVENTORY_QUANTITY_INVALID);
        if (newQuantity < reservedQuantity)
            throw new DomainException(ErrorCode.INVENTORY_INSUFFICIENT_STOCK);
        this.quantity = newQuantity;
    }

    public void setPricing(BigDecimal sellerPrice, BigDecimal purchasePrice) {
        if (sellerPrice != null && sellerPrice.signum() < 0)
            throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        if (purchasePrice != null && purchasePrice.signum() < 0)
            throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        this.sellerPrice = sellerPrice;
        this.purchasePrice = purchasePrice;
    }

    public void changeLowStockThreshold(int threshold) {
        if (threshold < 0)
            throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        this.lowStockThreshold = threshold;
    }

    public boolean isLowStock() {
        return availableQuantity() <= lowStockThreshold;
    }

    private static void requirePositive(int amount) {
        if (amount <= 0)
            throw new DomainException(ErrorCode.INVENTORY_QUANTITY_INVALID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}