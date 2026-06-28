package com.azercell.marketplace.orders.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.orders.application.service.OrderService;
import com.azercell.marketplace.orders.web.dto.request.PlaceOrderRequest;
import com.azercell.marketplace.orders.web.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/** Employee-facing orders: place an order, view it, and list your own orders. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> place(@Valid @RequestBody PlaceOrderRequest request) {
        return new ResponseEntity<>(ApiResponse.created(orderService.placeOrder(request)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> listMine(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.listMyOrders(page, size)));
    }
}