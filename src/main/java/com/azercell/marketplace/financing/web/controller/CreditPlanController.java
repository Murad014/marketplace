package com.azercell.marketplace.financing.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.financing.application.service.CreditPlanService;
import com.azercell.marketplace.financing.web.dto.response.InstallmentOptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/** Public installment-quote endpoint used by the storefront ("from X / month"). */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/credit-plans")
public class CreditPlanController {

    private final CreditPlanService creditPlanService;

    @GetMapping("/quote")
    public ResponseEntity<ApiResponse<List<InstallmentOptionResponse>>> quote(
            @RequestParam BigDecimal price) {
        return ResponseEntity.ok(ApiResponse.ok(creditPlanService.quote(price)));
    }
}