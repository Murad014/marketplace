package com.azercell.marketplace.orders.domain;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * A line in an order. Product/colour/price details are snapshotted at purchase time so the
 * order is immutable even if the catalog later changes.
 */
@Getter
public class OrderItem {

    private final UUID id;
    private final UUID variantId;
    private final String productName;
    private final String productSku;
    private final String colorName;
    private final BigDecimal originalPrice;   // regular price before promo
    private final BigDecimal unitPrice;       // effective price paid (after promo)
    private final int quantity;
    private final BigDecimal lineTotal;       // unitPrice * quantity
    private final boolean wasPromo;
    private final String promoLabel;

    private OrderItem(UUID id, UUID variantId, String productName, String productSku, String colorName,
                      BigDecimal originalPrice, BigDecimal unitPrice, int quantity,
                      BigDecimal lineTotal, boolean wasPromo, String promoLabel) {
        this.id = id;
        this.variantId = variantId;
        this.productName = productName;
        this.productSku = productSku;
        this.colorName = colorName;
        this.originalPrice = originalPrice;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
        this.wasPromo = wasPromo;
        this.promoLabel = promoLabel;
    }

    public static OrderItem create(UUID variantId, String productName, String productSku, String colorName,
                                   BigDecimal originalPrice, BigDecimal unitPrice, int quantity,
                                   boolean wasPromo, String promoLabel) {
        if (variantId == null) throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        if (quantity <= 0) throw new DomainException(ErrorCode.ORDER_ITEM_QUANTITY_INVALID);
        if (unitPrice == null || originalPrice == null) throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        var lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return new OrderItem(UUID.randomUUID(), variantId, productName, productSku, colorName,
                originalPrice, unitPrice, quantity, lineTotal, wasPromo, promoLabel);
    }

    public static OrderItem rehydrate(UUID id, UUID variantId, String productName, String productSku,
                                      String colorName, BigDecimal originalPrice, BigDecimal unitPrice,
                                      int quantity, BigDecimal lineTotal, boolean wasPromo, String promoLabel) {
        return new OrderItem(id, variantId, productName, productSku, colorName, originalPrice, unitPrice,
                quantity, lineTotal, wasPromo, promoLabel);
    }

    /** Regular value of this line (before promo) — used for the order subtotal. */
    public BigDecimal originalLineTotal() {
        return originalPrice.multiply(BigDecimal.valueOf(quantity));
    }
}