package com.azercell.marketplace.inventory.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to create a warehouse")
public record CreateWarehouseRequest(
        @Schema(description = "Warehouse name", example = "Baku Main", maxLength = 120)
        @NotBlank @Size(max = 120) String name,
        @Schema(description = "Unique code, 2–30 chars (letters, digits, hyphen)", example = "BAKU-A", maxLength = 30)
        @NotBlank @Size(max = 30) String code,
        @Schema(description = "Optional location/address", example = "Baku, 28 May st.", nullable = true)
        @Size(max = 200) String location
) {
}
