package com.azercell.marketplace.catalog.web.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductSummaryResponse(
        UUID id,
        String sku,
        String name,
        UUID brandId,
        UUID categoryId,
        BigDecimal basePrice,
        BigDecimal promoPrice,
        BigDecimal sellingPrice,
        String priceCurrency,
        String availability,
        String thumbnailUrl,
        List<ColorOptionResponse> colorOptions
) {
}