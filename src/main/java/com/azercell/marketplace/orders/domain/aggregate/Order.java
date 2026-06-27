package com.azercell.marketplace.orders.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.orders.domain.OrderItem;
import com.azercell.marketplace.orders.domain.vo.OrderStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Order {

    private static final DateTimeFormatter ORDER_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final UUID id;
    private final String orderNumber;
    private final UUID userId;
    private final UUID warehouseId;
    private OrderStatus status;
    private final List<OrderItem> items;
    private final BigDecimal subtotalAmount;   // sum of original prices x qty
    private final BigDecimal discountAmount;   // total promo savings
    private final BigDecimal totalAmount;      // what the employee actually pays
    private final String currency;
    private final LocalDateTime placedAt;

    private Order(UUID id, String orderNumber, UUID userId, UUID warehouseId, OrderStatus status,
                  List<OrderItem> items, BigDecimal subtotalAmount, BigDecimal discountAmount,
                  BigDecimal totalAmount, String currency, LocalDateTime placedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.warehouseId = warehouseId;
        this.status = status;
        this.items = new ArrayList<>(items);
        this.subtotalAmount = subtotalAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.placedAt = placedAt;
    }

    /** Place a new order from snapshot line items. Computes amounts and starts at PENDING. */
    public static Order place(UUID userId, UUID warehouseId, List<OrderItem> items, String currency) {
        if (userId == null || warehouseId == null) throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        if (items == null || items.isEmpty()) throw new DomainException(ErrorCode.ORDER_EMPTY);

        BigDecimal subtotal = items.stream()
                .map(OrderItem::originalLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discount = subtotal.subtract(total);

        var id = UUID.randomUUID();
        return new Order(id, generateOrderNumber(id), userId, warehouseId, OrderStatus.PENDING,
                items, subtotal, discount, total, currency == null ? "AZN" : currency, LocalDateTime.now());
    }

    public static Order rehydrate(UUID id, String orderNumber, UUID userId, UUID warehouseId,
                                  OrderStatus status, List<OrderItem> items, BigDecimal subtotalAmount,
                                  BigDecimal discountAmount, BigDecimal totalAmount, String currency,
                                  LocalDateTime placedAt) {
        return new Order(id, orderNumber, userId, warehouseId, status, items, subtotalAmount,
                discountAmount, totalAmount, currency, placedAt);
    }

    /** Move the order to a new status if the transition is legal; returns the previous status. */
    public OrderStatus transitionTo(OrderStatus target) {
        if (target == null) throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        if (!status.canTransitionTo(target))
            throw new DomainException(ErrorCode.ORDER_INVALID_STATUS_TRANSITION);
        OrderStatus previous = this.status;
        this.status = target;
        return previous;
    }

    /** Stock is reserved while the order is still cancellable (not yet shipped/delivered). */
    public boolean holdsReservedStock() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED || status == OrderStatus.PROCESSING;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    private static String generateOrderNumber(UUID id) {
        return "ORD-" + LocalDateTime.now().format(ORDER_DATE) + "-"
                + id.toString().replace("-", "").substring(0, 6).toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}