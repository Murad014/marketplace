package com.azercell.marketplace.inventory.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateWarehouseRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 30) String code,
        @Size(max = 200) String location
) {
}