package com.azercell.marketplace.orders.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.orders.web.dto.request.PlaceOrderRequest;
import com.azercell.marketplace.orders.web.dto.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Orders", description = "Employee-facing: place an order, view it, and list your own orders. The user comes from the token.")
public interface OrderApi {

    @Operation(summary = "Place an order",
            description = "Snapshots prices, reserves stock per line, and (if creditPlanId is set) opens a financing "
                    + "agreement on the total. warehouseId optional (defaults to primary). The user is taken from the token.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Order placed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Validation error, variant not found, insufficient stock, or no fulfilment warehouse")
    })
    ResponseEntity<ApiResponse<OrderResponse>> place(PlaceOrderRequest request);

    @Operation(summary = "Get an order by id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Order not found (ORDER_NOT_FOUND)")
    })
    ResponseEntity<ApiResponse<OrderResponse>> getById(@Parameter(description = "Order id") UUID id);

    @Operation(summary = "List my orders (paginated)", description = "Orders for the authenticated user.")
    ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> listMine(
            @Parameter(description = "Zero-based page index", example = "0") int page,
            @Parameter(description = "Page size", example = "20") int size);
}
