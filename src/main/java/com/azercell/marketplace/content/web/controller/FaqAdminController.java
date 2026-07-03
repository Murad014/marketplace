package com.azercell.marketplace.content.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.content.application.service.FaqService;
import com.azercell.marketplace.content.web.controller.api.FaqAdminApi;
import com.azercell.marketplace.content.web.dto.request.CreateFaqRequest;
import com.azercell.marketplace.content.web.dto.request.UpdateFaqRequest;
import com.azercell.marketplace.content.web.dto.response.FaqAdminResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/v1/admin/faqs")
public class FaqAdminController implements FaqAdminApi {

    private final FaqService faqService;

    @PostMapping
    public ResponseEntity<ApiResponse<FaqAdminResponse>> create(@Valid @RequestBody CreateFaqRequest request) {
        return new ResponseEntity<>(ApiResponse.created(faqService.create(request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FaqAdminResponse>> update(@PathVariable UUID id,
                                                                @Valid @RequestBody UpdateFaqRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(faqService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        faqService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Deleted"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FaqAdminResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(faqService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FaqAdminResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(faqService.getAll()));
    }
}
