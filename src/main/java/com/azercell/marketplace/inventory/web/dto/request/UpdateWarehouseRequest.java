package com.azercell.marketplace.inventory.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateWarehouseRequest(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 200) String location
) {
}