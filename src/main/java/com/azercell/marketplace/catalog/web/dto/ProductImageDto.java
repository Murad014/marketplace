package com.azercell.marketplace.catalog.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Valid
public record ProductImageDto(
        @NotEmpty(message = "Product Variant Image cannot be empty")
        String name,

        @NotEmpty(message = "Product Variant Image url cannot be empty")
        String url,

        @NotEmpty(message = "Product Variant Image alt text cannot be empty")
        String altText,

        Boolean isPrimary,

        @NotNull(message = "Product Variant Image sort cannot be null")
        @Size()
        Integer sort
) {
}
