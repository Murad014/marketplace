package com.azercell.marketplace.financing.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.financing.web.dto.response.InstallmentOptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Credit Plans (Public)", description = "Public installment quoting for a given price.")
public interface CreditPlanQuoteApi {

    @Operation(summary = "Quote installment options for a price",
            description = "Returns each active plan's monthly installment, interest and total payable for the given price.")
    ResponseEntity<ApiResponse<List<InstallmentOptionResponse>>> quote(
            @Parameter(description = "Price to quote against", example = "1200") BigDecimal price);
}
