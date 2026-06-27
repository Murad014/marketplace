package com.azercell.marketplace.catalog.web.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String sku,
        String name,
        UUID brandId,
        String description,
        BigDecimal basePrice,
        BigDecimal promoPrice,
        BigDecimal sellingPrice,
        String priceCurrency,
        Object specs,
        UUID categoryId,
        String availability,
        String status,
        Set<UUID> creditPlans,
        List<InstallmentOption> installmentOptions,
        List<VariantResponse> variants
) {

    public record InstallmentOption(
            UUID planId,
            String name,
            int months,
            BigDecimal interestRate,
            BigDecimal monthlyInstallment,
            BigDecimal totalPayable
    ) {}

    public record VariantResponse(
            UUID id,
            String sku,
            ColorResponse color,
            BigDecimal priceOverride,
            String status,
            Object specs,
            List<ImageResponse> images
    ) {}

    public record ColorResponse(
            UUID id,
            String name,
            String hexCode,
            String status
    ) {}

    public record ImageResponse(
            UUID id,
            String name,
            String url,
            String altText,
            boolean isPrimary,
            int sortOrder,
            String status
    ) {}
}