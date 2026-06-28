package com.azercell.marketplace.catalog.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBrandRequest(
        @NotBlank @Size(max = 140) String name,
        @NotBlank @Size(min = 2, max = 20) String code
) {
}
