package com.azercell.marketplace.financing.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.financing.web.dto.request.CreateCreditPlanRequest;
import com.azercell.marketplace.financing.web.dto.request.UpdateCreditPlanRequest;
import com.azercell.marketplace.financing.web.dto.response.CreditPlanResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Credit Plans (Admin)", description = "Manage installment credit plans (months + interest rate, DEFAULT/OPTIONAL).")
public interface CreditPlanAdminApi {

    @Operation(summary = "Create a credit plan",
            description = "type null = OPTIONAL (opt-in); DEFAULT plans are applied to products that specify none.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Credit plan created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error")
    })
    ResponseEntity<ApiResponse<CreditPlanResponse>> create(CreateCreditPlanRequest request);

    @Operation(summary = "Update a credit plan")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error or plan not found")
    })
    ResponseEntity<ApiResponse<CreditPlanResponse>> update(
            @Parameter(description = "Credit plan id") UUID id, UpdateCreditPlanRequest request);

    @Operation(summary = "Get a credit plan by id")
    ResponseEntity<ApiResponse<CreditPlanResponse>> getById(@Parameter(description = "Credit plan id") UUID id);

    @Operation(summary = "List all credit plans")
    ResponseEntity<ApiResponse<List<CreditPlanResponse>>> list();

    @Operation(summary = "Activate a credit plan")
    ResponseEntity<ApiResponse<CreditPlanResponse>> activate(@Parameter(description = "Credit plan id") UUID id);

    @Operation(summary = "Deactivate a credit plan")
    ResponseEntity<ApiResponse<CreditPlanResponse>> deactivate(@Parameter(description = "Credit plan id") UUID id);
}
