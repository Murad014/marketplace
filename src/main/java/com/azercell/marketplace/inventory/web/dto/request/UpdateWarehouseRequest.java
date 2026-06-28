package com.azercell.marketplace.inventory.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to update a warehouse (name/location; status is changed via the activate/deactivate/primary endpoints)")
public record UpdateWarehouseRequest(
        @Schema(description = "Warehouse name", example = "Baku Main", maxLength = 120)
        @NotBlank @Size(max = 120) String name,
        @Schema(description = "Optional location/address", nullable = true)
        @Size(max = 200) String location
) {
}
