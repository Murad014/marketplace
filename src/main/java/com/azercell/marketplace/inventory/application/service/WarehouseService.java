package com.azercell.marketplace.inventory.application.service;

import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.inventory.web.dto.request.CreateWarehouseRequest;
import com.azercell.marketplace.inventory.web.dto.request.UpdateWarehouseRequest;
import com.azercell.marketplace.inventory.web.dto.response.WarehouseResponse;

import java.util.UUID;

public interface WarehouseService {
    WarehouseResponse create(CreateWarehouseRequest request);
    WarehouseResponse update(UUID id, UpdateWarehouseRequest request);
    WarehouseResponse getById(UUID id);
    PageResponse<WarehouseResponse> list(int page, int size);
    WarehouseResponse activate(UUID id);
    WarehouseResponse deactivate(UUID id);
    WarehouseResponse setPrimary(UUID id);
}