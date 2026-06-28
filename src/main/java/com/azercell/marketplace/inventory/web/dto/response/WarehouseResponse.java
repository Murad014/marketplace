package com.azercell.marketplace.inventory.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "A warehouse (stock location)")
public record WarehouseResponse(
        @Schema(description = "Warehouse id") UUID id,
        @Schema(description = "Name", example = "Baku Main") String name,
        @Schema(description = "Code", example = "BAKU-A") String code,
        @Schema(description = "Location", nullable = true) String location,
        @Schema(description = "Active flag", example = "true") boolean active,
        @Schema(description = "Whether this is the single primary warehouse", example = "true") boolean primary
) {
}
