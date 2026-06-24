package com.azercell.marketplace.catalog.web.dto.request;

import com.azercell.marketplace.catalog.domain.vo.Availability;
import com.azercell.marketplace.catalog.web.dto.ProductVariantDto;
import com.azercell.marketplace.catalog.web.dto.SpecificationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Valid
public record AddProductRequest(
        @NotNull(message = "category id must be selected")
        UUID categoryId,

        @NotEmpty(message = "Product name cannot bu empty")
        String name,

        BigDecimal promoPrice,

        @NotNull(message = "Product brand cannot be null")
        UUID brandId,

        String description,

        @NotNull(message = "Price cannot be null")
        BigDecimal price,

        @NotNull(message = "Availability cannot be null")
        Availability availability,

        @NotNull(message = "Product Color cannot be null.")
        @NotEmpty(message = "Product Color cannot be empty.")
        List<ProductVariantDto> productVariants,

        List<SpecificationDto> specifications
) {

}


