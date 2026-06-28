package com.azercell.marketplace.catalog.web.dto.request;

import com.azercell.marketplace.catalog.domain.vo.Availability;
import com.azercell.marketplace.catalog.web.dto.ProductVariantDto;
import com.azercell.marketplace.catalog.web.dto.SpecificationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Valid
@Schema(description = "Payload to create a product with its variants, specs and eligible credit plans")
public record AddProductRequest(
        @Schema(description = "Category id (must exist)")
        @NotNull(message = "category id must be selected")
        UUID categoryId,

        @Schema(description = "Product name", example = "iPhone 15 Pro")
        @NotEmpty(message = "Product name cannot bu empty")
        String name,

        @Schema(description = "Optional promo price; must be ≤ price", example = "1799.99", nullable = true)
        BigDecimal promoPrice,

        @Schema(description = "Brand id (must exist)")
        @NotNull(message = "Product brand cannot be null")
        UUID brandId,

        @Schema(description = "Optional long description", nullable = true)
        String description,

        @Schema(description = "Base price", example = "1999.99")
        @NotNull(message = "Price cannot be null")
        BigDecimal price,

        @Schema(description = "Availability state", example = "ORDER_NOW")
        @NotNull(message = "Availability cannot be null")
        Availability availability,

        @Schema(description = "One entry per colour variant (with images and stockCount)")
        @NotNull(message = "Product Color cannot be null.")
        @NotEmpty(message = "Product Color cannot be empty.")
        List<ProductVariantDto> productVariants,

        @Schema(description = "Optional key/value specifications", nullable = true)
        List<SpecificationDto> specifications,

        @Schema(description = "Eligible credit plan ids; null/empty defaults to all active plans", nullable = true)
        List<UUID> creditPlanIds
) {

}


