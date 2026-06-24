package com.azercell.marketplace.catalog.web.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Valid
public record ProductVariantDto(
        @NotEmpty(message = "Color name cannot be null")
        String colorName,

        @NotEmpty(message = "Color hex code cannot be null")
        String colorHexCode,

        @NotEmpty(message = "Stock count cannot be empty")
        @Size(min = 1, message = "Stock count must be at least 1")
        Integer stockCount,

        @NotNull(message = "Product Variant Images cannot be null")
        @NotEmpty(message = "Product Variant Images cannot be empty")
        List<ProductImageDto> images

){

}
