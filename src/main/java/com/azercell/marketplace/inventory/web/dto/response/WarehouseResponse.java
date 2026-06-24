package com.azercell.marketplace.inventory.web.dto.response;

import java.util.UUID;

public record WarehouseResponse(
        UUID id,
        String name,
        String code,
        String location,
        boolean active,
        boolean primary
) {
}