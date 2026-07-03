package com.azercell.marketplace.content.application.service;

import com.azercell.marketplace.content.web.dto.request.CreateFaqRequest;
import com.azercell.marketplace.content.web.dto.request.UpdateFaqRequest;
import com.azercell.marketplace.content.web.dto.response.FaqAdminResponse;
import com.azercell.marketplace.content.web.dto.response.FaqResponse;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface FaqService {

    FaqAdminResponse create(CreateFaqRequest request);

    FaqAdminResponse update(UUID id, UpdateFaqRequest request);

    void delete(UUID id);

    FaqAdminResponse getById(UUID id);

    List<FaqAdminResponse> getAll();

    /** Public storefront read: active entries only, each localized to {@code locale} (az → AZ, else EN). */
    List<FaqResponse> getPublished(Locale locale);
}
