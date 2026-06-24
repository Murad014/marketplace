package com.azercell.marketplace.inventory.application.service.impl;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.inventory.application.port.WarehouseRepository;
import com.azercell.marketplace.inventory.application.service.WarehouseService;
import com.azercell.marketplace.inventory.domain.aggregate.Warehouse;
import com.azercell.marketplace.inventory.web.dto.request.CreateWarehouseRequest;
import com.azercell.marketplace.inventory.web.dto.request.UpdateWarehouseRequest;
import com.azercell.marketplace.inventory.web.dto.response.WarehouseResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private static final int MAX_PAGE_SIZE = 100;

    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public WarehouseResponse create(CreateWarehouseRequest request) {
        var warehouse = Warehouse.create(request.name(), request.code(), request.location());
        if (warehouseRepository.existsByCode(warehouse.getCode()))
            throw new DomainException(ErrorCode.WAREHOUSE_CODE_ALREADY_EXISTS);
        return toResponse(warehouseRepository.save(warehouse));
    }

    @Override
    @Transactional
    public WarehouseResponse update(UUID id, UpdateWarehouseRequest request) {
        var warehouse = load(id);
        warehouse.changeName(request.name());
        warehouse.changeLocation(request.location());
        return toResponse(warehouseRepository.save(warehouse));
    }

    @Override
    @Transactional
    public WarehouseResponse getById(UUID id) {
        return toResponse(load(id));
    }

    @Override
    @Transactional
    public PageResponse<WarehouseResponse> list(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 20 : Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("name").ascending());
        return PageResponse.of(warehouseRepository.findAll(pageable).map(this::toResponse));
    }

    @Override
    @Transactional
    public WarehouseResponse activate(UUID id) {
        var warehouse = load(id);
        warehouse.activate();
        return toResponse(warehouseRepository.save(warehouse));
    }

    @Override
    @Transactional
    public WarehouseResponse deactivate(UUID id) {
        var warehouse = load(id);
        warehouse.deactivate();
        return toResponse(warehouseRepository.save(warehouse));
    }

    /** Promote a warehouse to primary, demoting whichever warehouse is currently primary. */
    @Override
    @Transactional
    public WarehouseResponse setPrimary(UUID id) {
        var target = load(id);

        warehouseRepository.findPrimary().ifPresent(current -> {
            if (!current.getId().equals(target.getId())) {
                current.unmarkAsPrimary();
                warehouseRepository.save(current);
            }
        });

        target.markAsPrimary();
        return toResponse(warehouseRepository.save(target));
    }

    // <editor-fold desc="privateHelperMethods">
    private Warehouse load(UUID id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.WAREHOUSE_NOT_FOUND));
    }

    private WarehouseResponse toResponse(Warehouse w) {
        return new WarehouseResponse(
                w.getId(),
                w.getName(),
                w.getCode(),
                w.getLocation(),
                w.isActive(),
                w.isPrimary());
    }
    // </editor-fold>
}