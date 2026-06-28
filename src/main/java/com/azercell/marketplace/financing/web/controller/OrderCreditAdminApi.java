package com.azercell.marketplace.financing.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.financing.web.dto.response.OrderCreditResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Order Credits (Admin)", description = "Financing agreement and installment schedule for an order.")
public interface OrderCreditAdminApi {

    @Operation(summary = "Get the order credit by order id",
            description = "Returns the financing agreement + installment schedule. 400 if the order had no plan (ORDER_CREDIT_NOT_FOUND).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "No financing agreement for this order")
    })
    ResponseEntity<ApiResponse<OrderCreditResponse>> getByOrder(
            @Parameter(description = "Order id") UUID orderId);

    @Operation(summary = "Get the order credit by its id")
    ResponseEntity<ApiResponse<OrderCreditResponse>> getById(@Parameter(description = "Order credit id") UUID id);

    @Operation(summary = "Pay the next installment",
            description = "Marks the earliest unpaid installment as paid; completes the agreement when all are paid.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Installment paid"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Not found or no unpaid installment")
    })
    ResponseEntity<ApiResponse<OrderCreditResponse>> payNext(@Parameter(description = "Order credit id") UUID id);
}
