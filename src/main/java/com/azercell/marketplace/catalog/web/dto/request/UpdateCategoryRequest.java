package com.azercell.marketplace.catalog.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Full update. {@code parentId} null = make this a root category. {@code active} is optional —
 * null leaves the current status untouched.
 */
public record UpdateCategoryRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 140) String slug,
        @Size(max = 2000) String description,
        UUID parentId,
        Boolean active
) {
}
