package com.azercell.marketplace.orders.application.service;

import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.orders.web.dto.request.PlaceOrderRequest;
import com.azercell.marketplace.orders.web.dto.request.UpdateOrderStatusRequest;
import com.azercell.marketplace.orders.web.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse placeOrder(PlaceOrderRequest request);
    OrderResponse getById(UUID id);
    PageResponse<OrderResponse> listByUser(UUID userId, int page, int size);
    PageResponse<OrderResponse> listAll(int page, int size);
    OrderResponse updateStatus(UUID id, UpdateOrderStatusRequest request);
}