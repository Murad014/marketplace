package com.azercell.marketplace.content.web.controller;

import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.content.application.service.FaqService;
import com.azercell.marketplace.content.web.controller.api.FaqApi;
import com.azercell.marketplace.content.web.dto.response.FaqResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Public, employee-facing FAQ listing. Only active entries are exposed, localized via the
 * request's Accept-Language (resolved into {@link LocaleContextHolder}). Admin authoring lives
 * under {@link FaqAdminController}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faqs")
public class FaqController implements FaqApi {

    private final FaqService faqService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FaqResponse>>> list() {
        var faqs = faqService.getPublished(LocaleContextHolder.getLocale());
        return ResponseEntity.ok(ApiResponse.ok(faqs));
    }
}
