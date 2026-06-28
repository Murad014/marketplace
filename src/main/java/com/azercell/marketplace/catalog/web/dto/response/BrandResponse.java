package com.azercell.marketplace.catalog.web.dto.response;

import java.util.UUID;

public record BrandResponse(
        UUID id,
        String name,
        String code,
        String status
) {
}
