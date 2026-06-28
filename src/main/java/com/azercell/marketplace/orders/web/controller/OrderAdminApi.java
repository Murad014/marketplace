package com.azercell.marketplace.orders.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.orders.web.dto.request.UpdateOrderStatusRequest;
import com.azercell.marketplace.orders.web.dto.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Orders (Admin)", description = "Back-office: list all orders and drive the order status lifecycle.")
public interface OrderAdminApi {

    @Operation(summary = "List all orders (paginated)")
    ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> list(
            @Parameter(description = "Zero-based page index", example = "0") int page,
            @Parameter(description = "Page size", example = "20") int size);

    @Operation(summary = "Update order status",
            description = "Transitions: PENDING→CONFIRMED→PROCESSING→SHIPPED→DELIVERED (any non-terminal → CANCELLED). "
                    + "SHIPPED ships reserved stock; CANCELLED releases it.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Order not found or invalid transition (ORDER_INVALID_STATUS_TRANSITION)")
    })
    ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @Parameter(description = "Order id") UUID id, UpdateOrderStatusRequest request);
}
