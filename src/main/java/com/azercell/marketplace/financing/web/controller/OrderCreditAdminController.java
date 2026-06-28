package com.azercell.marketplace.financing.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.financing.application.service.OrderCreditService;
import com.azercell.marketplace.financing.web.dto.response.OrderCreditResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/order-credits")
public class OrderCreditAdminController implements OrderCreditAdminApi {

    private final OrderCreditService orderCreditService;

    /** The financing agreement + schedule for an order. */
    @GetMapping
    public ResponseEntity<ApiResponse<OrderCreditResponse>> getByOrder(@RequestParam UUID orderId) {
        return ResponseEntity.ok(ApiResponse.ok(orderCreditService.getByOrderId(orderId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderCreditResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(orderCreditService.getById(id)));
    }

    /** Record a monthly payment against the earliest unpaid installment. */
    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<OrderCreditResponse>> payNext(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(orderCreditService.payNextInstallment(id)));
    }
}