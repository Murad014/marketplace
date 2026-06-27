package com.azercell.marketplace.orders.application.service.impl;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.orders.application.port.CatalogApi;
import com.azercell.marketplace.orders.application.port.FinancingApi;
import com.azercell.marketplace.orders.application.port.InventoryApi;
import com.azercell.marketplace.orders.application.port.OrderRepository;
import com.azercell.marketplace.orders.application.port.OrderStatusHistoryRepository;
import com.azercell.marketplace.orders.application.service.OrderService;
import com.azercell.marketplace.orders.domain.OrderItem;
import com.azercell.marketplace.orders.domain.aggregate.Order;
import com.azercell.marketplace.orders.domain.aggregate.OrderStatusHistory;
import com.azercell.marketplace.orders.domain.vo.OrderStatus;
import com.azercell.marketplace.orders.web.dto.request.PlaceOrderRequest;
import com.azercell.marketplace.orders.web.dto.request.UpdateOrderStatusRequest;
import com.azercell.marketplace.orders.web.dto.response.OrderResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final int MAX_PAGE_SIZE = 100;

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final CatalogApi catalogApi;
    private final InventoryApi inventoryApi;
    private final FinancingApi financingApi;

    @Override
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        UUID warehouseId = request.warehouseId() != null ? request.warehouseId() : inventoryApi.primaryWarehouseId();
        if (warehouseId == null)
            throw new DomainException(ErrorCode.ORDER_NO_FULFILMENT_WAREHOUSE);

        // Snapshot each line from the catalog (price/name/colour frozen at purchase time).
        List<OrderItem> items = new ArrayList<>();
        for (var line : request.items()) {
            var info = catalogApi.findOrderLineInfo(line.variantId())
                    .orElseThrow(() -> new DomainException(ErrorCode.ORDER_VARIANT_NOT_FOUND));
            items.add(OrderItem.create(
                    info.variantId(), info.productName(), info.productSku(), info.colorName(),
                    info.originalPrice(), info.unitPrice(), line.quantity(),
                    info.wasPromo(), info.promoLabel()));
        }

        var order = Order.place(request.userId(), warehouseId, items, "AZN");

        // Reserve stock for every line; insufficient stock rolls the whole order back.
        var reference = "ORDER:" + order.getOrderNumber();
        for (var item : order.getItems()) {
            inventoryApi.reserve(warehouseId, item.getVariantId(), item.getQuantity(), reference);
        }

        var saved = orderRepository.save(order);
        historyRepository.save(OrderStatusHistory.record(
                saved.getId(), null, saved.getStatus(), "Order placed", request.userId()));

        // If the employee chose an installment plan, open the financing agreement on the order total.
        if (request.creditPlanId() != null) {
            financingApi.createOrderCredit(saved.getId(), request.creditPlanId(), saved.getTotalAmount());
        }

        return toResponse(saved);
    }

    @Override
    @Transactional
    public OrderResponse getById(UUID id) {
        return toResponse(load(id));
    }

    @Override
    @Transactional
    public PageResponse<OrderResponse> listByUser(UUID userId, int page, int size) {
        return PageResponse.of(orderRepository.findByUser(userId, pageable(page, size)).map(this::toResponse));
    }

    @Override
    @Transactional
    public PageResponse<OrderResponse> listAll(int page, int size) {
        return PageResponse.of(orderRepository.findAll(pageable(page, size)).map(this::toResponse));
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(UUID id, UpdateOrderStatusRequest request) {
        var order = load(id);
        var reference = "ORDER:" + order.getOrderNumber();
        boolean wasHoldingStock = order.holdsReservedStock();

        OrderStatus from = order.transitionTo(request.status());

        // Apply the stock effect of the new status.
        if (request.status() == OrderStatus.SHIPPED) {
            order.getItems().forEach(i ->
                    inventoryApi.ship(order.getWarehouseId(), i.getVariantId(), i.getQuantity(), reference));
        } else if (request.status() == OrderStatus.CANCELLED && wasHoldingStock) {
            order.getItems().forEach(i ->
                    inventoryApi.release(order.getWarehouseId(), i.getVariantId(), i.getQuantity(), reference));
        }

        var saved = orderRepository.save(order);
        historyRepository.save(OrderStatusHistory.record(
                saved.getId(), from, saved.getStatus(), request.note(), request.changedBy()));

        return toResponse(saved);
    }

    // <editor-fold desc="privateHelperMethods">
    private Order load(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.ORDER_NOT_FOUND));
    }

    private Pageable pageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 20 : Math.min(size, MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by("placedAt").descending());
    }

    private OrderResponse toResponse(Order order) {
        var items = order.getItems().stream()
                .map(i -> new OrderResponse.Item(
                        i.getId(), i.getVariantId(), i.getProductName(), i.getProductSku(), i.getColorName(),
                        i.getOriginalPrice(), i.getUnitPrice(), i.getQuantity(), i.getLineTotal(),
                        i.isWasPromo(), i.getPromoLabel()))
                .toList();

        var history = historyRepository.findByOrderId(order.getId()).stream()
                .map(h -> new OrderResponse.StatusHistory(
                        h.getFromStatus() != null ? h.getFromStatus().name() : null,
                        h.getToStatus().name(), h.getNote(), h.getChangedBy(), h.getCreatedAt()))
                .toList();

        return new OrderResponse(
                order.getId(), order.getOrderNumber(), order.getUserId(), order.getWarehouseId(),
                order.getStatus().name(), order.getSubtotalAmount(), order.getDiscountAmount(),
                order.getTotalAmount(), order.getCurrency(), order.getPlacedAt(), items, history);
    }
    // </editor-fold>
}