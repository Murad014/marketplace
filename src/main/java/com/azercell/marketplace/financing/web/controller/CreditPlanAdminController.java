package com.azercell.marketplace.financing.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.financing.application.service.CreditPlanService;
import com.azercell.marketplace.financing.web.dto.request.CreateCreditPlanRequest;
import com.azercell.marketplace.financing.web.dto.request.UpdateCreditPlanRequest;
import com.azercell.marketplace.financing.web.dto.response.CreditPlanResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/credit-plans")
public class CreditPlanAdminController {

    private final CreditPlanService creditPlanService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreditPlanResponse>> create(
            @Valid @RequestBody CreateCreditPlanRequest request) {
        return new ResponseEntity<>(ApiResponse.created(creditPlanService.create(request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CreditPlanResponse>> update(
            @PathVariable UUID id, @Valid @RequestBody UpdateCreditPlanRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(creditPlanService.update(id, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CreditPlanResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(creditPlanService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CreditPlanResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(creditPlanService.list()));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<CreditPlanResponse>> activate(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(creditPlanService.activate(id)));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<CreditPlanResponse>> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(creditPlanService.deactivate(id)));
    }
}
