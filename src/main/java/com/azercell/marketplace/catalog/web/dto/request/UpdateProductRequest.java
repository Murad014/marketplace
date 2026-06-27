package com.azercell.marketplace.catalog.web.dto.request;

import com.azercell.marketplace.catalog.domain.vo.Availability;
import com.azercell.marketplace.catalog.web.dto.SpecificationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record UpdateProductRequest(
        @NotBlank String name,
        @NotNull UUID brandId,
        String description,
        @NotNull @Positive BigDecimal price,
        BigDecimal promoPrice,
        List<SpecificationDto> specifications,
        @NotNull UUID categoryId,
        @NotNull Availability availability,
        @NotEmpty @Valid List<VariantPart> variants,
        // Eligible credit plans; null/empty -> defaults to all active plans.
        List<UUID> creditPlanIds
) {
    public record VariantPart(
            UUID id,                       // null = new variant
            @NotBlank
            String sku,
            @NotBlank String colorName,
            @NotBlank String colorHexCode,
            BigDecimal priceOverride,
            List<SpecificationDto> specifications,
            @Valid List<ImagePart> images
    ) {}

    public record ImagePart(
            UUID id,                       // null = new image
            @NotBlank String url,
            String name,
            String altText,
            boolean isPrimary,
            @PositiveOrZero int sort
    ) {}
}