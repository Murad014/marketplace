package com.azercell.marketplace.catalog.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** All fields applied on update. {@code active} is optional — null leaves the current status untouched. */
public record UpdateBrandRequest(
        @NotBlank @Size(max = 140) String name,
        @NotBlank @Size(min = 2, max = 20) String code,
        Boolean active
) {
}
