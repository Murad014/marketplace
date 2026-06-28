package com.azercell.marketplace.catalog.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(description = "Product listing card (storefront)")
public record ProductSummaryResponse(
        @Schema(description = "Product id") UUID id,
        @Schema(description = "SKU", example = "IPHONE-15-PRO-7C2A373E") String sku,
        @Schema(description = "Name", example = "iPhone 15 Pro") String name,
        @Schema(description = "Brand id") UUID brandId,
        @Schema(description = "Category id") UUID categoryId,
        @Schema(description = "Base price", example = "1999.99") BigDecimal basePrice,
        @Schema(description = "Promo price if any", example = "1799.99", nullable = true) BigDecimal promoPrice,
        @Schema(description = "Effective selling price (promo if set, else base)", example = "1799.99") BigDecimal sellingPrice,
        @Schema(description = "Currency", example = "AZN") String priceCurrency,
        @Schema(description = "Cheapest monthly installment across eligible plans (\"from X/month\"); null if none", example = "157.50", nullable = true) BigDecimal monthlyFrom,
        @Schema(description = "Longest available term in months; null if none", example = "12", nullable = true) Integer maxMonths,
        @Schema(description = "Availability state", example = "ORDER_NOW") String availability,
        @Schema(description = "Thumbnail image URL", nullable = true) String thumbnailUrl,
        @Schema(description = "Selectable colour options (swatch + image)") List<ColorOptionResponse> colorOptions
) {
}