package com.azercell.marketplace.orders.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.orders.application.service.OrderService;
import com.azercell.marketplace.orders.web.dto.request.UpdateOrderStatusRequest;
import com.azercell.marketplace.orders.web.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/** Admin order management: list all orders and drive status transitions. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/orders")
public class OrderAdminController implements OrderAdminApi {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.listAll(page, size)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable UUID id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.updateStatus(id, request)));
    }
}