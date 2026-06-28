package com.azercell.marketplace.catalog.web.dto.response;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String slug,
        String description,
        String status,
        UUID parentId
) {
}
