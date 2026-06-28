package com.azercell.marketplace.inventory.application.service.impl;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.dto.PageResponse;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.common.security.CurrentUserProvider;
import com.azercell.marketplace.inventory.application.port.InventoryMovementRepository;
import com.azercell.marketplace.inventory.application.port.InventoryRepository;
import com.azercell.marketplace.inventory.application.service.InventoryService;
import com.azercell.marketplace.inventory.domain.aggregate.Inventory;
import com.azercell.marketplace.inventory.domain.aggregate.InventoryMovement;
import com.azercell.marketplace.inventory.domain.vo.MovementType;
import com.azercell.marketplace.inventory.web.dto.request.AdjustStockRequest;
import com.azercell.marketplace.inventory.web.dto.request.RestockRequest;
import com.azercell.marketplace.inventory.web.dto.response.InventoryMovementResponse;
import com.azercell.marketplace.inventory.web.dto.response.InventoryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final int MAX_PAGE_SIZE = 100;

    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository movementRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public void seedStock(UUID warehouseId, UUID variantId, int quantity, String reference) {
        if (quantity <= 0) return;

        var inventory = inventoryRepository.findByWarehouseAndVariant(warehouseId, variantId)
                .map(existing -> {
                    existing.restock(quantity);
                    return existing;
                })
                .orElseGet(() -> Inventory.create(warehouseId, variantId, quantity));

        inventoryRepository.save(inventory);
        recordMovement(warehouseId, variantId, quantity, MovementType.RESTOCK, reference);
    }

    @Override
    @Transactional
    public void reserve(UUID warehouseId, UUID variantId, int quantity, String reference) {
        var inventory = load(warehouseId, variantId);
        inventory.reserve(quantity);
        inventoryRepository.save(inventory);
        recordMovement(warehouseId, variantId, -quantity, MovementType.RESERVE, reference);
    }

    @Override
    @Transactional
    public void release(UUID warehouseId, UUID variantId, int quantity, String reference) {
        var inventory = load(warehouseId, variantId);
        inventory.release(quantity);
        inventoryRepository.save(inventory);
        recordMovement(warehouseId, variantId, quantity, MovementType.RELEASE, reference);
    }

    @Override
    @Transactional
    public void ship(UUID warehouseId, UUID variantId, int quantity, String reference) {
        var inventory = load(warehouseId, variantId);
        inventory.shipReserved(quantity);
        inventoryRepository.save(inventory);
        recordMovement(warehouseId, variantId, -quantity, MovementType.SALE, reference);
    }

    @Override
    @Transactional
    public InventoryResponse restock(RestockRequest request) {
        var inventory = load(request.warehouseId(), request.variantId());
        inventory.restock(request.amount());
        var saved = inventoryRepository.save(inventory);
        recordMovement(request.warehouseId(), request.variantId(), request.amount(),
                MovementType.RESTOCK, request.reference());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryResponse adjustQuantity(AdjustStockRequest request) {
        var inventory = load(request.warehouseId(), request.variantId());
        int delta = request.newQuantity() - inventory.getQuantity();
        inventory.adjustQuantity(request.newQuantity());
        var saved = inventoryRepository.save(inventory);
        if (delta != 0) {
            recordMovement(request.warehouseId(), request.variantId(), delta,
                    MovementType.CORRECTION, request.reference());
        }
        return toResponse(saved);
    }

    @Override
    @Transactional
    public List<InventoryResponse> getByVariant(UUID variantId) {
        return inventoryRepository.findByVariant(variantId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public PageResponse<InventoryMovementResponse> getMovements(UUID variantId, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 20 : Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("createdDate").descending());
        return PageResponse.of(movementRepository.findByVariant(variantId, pageable).map(this::toMovementResponse));
    }

    // <editor-fold desc="privateHelperMethods">
    private Inventory load(UUID warehouseId, UUID variantId) {
        return inventoryRepository.findByWarehouseAndVariant(warehouseId, variantId)
                .orElseThrow(() -> new DomainException(ErrorCode.INVENTORY_NOT_FOUND));
    }

    private void recordMovement(UUID warehouseId, UUID variantId, int change, MovementType type, String reference) {
        var performedBy = currentUserProvider.currentUserId().orElse(null);
        movementRepository.save(InventoryMovement.record(warehouseId, variantId, change, type, reference, performedBy));
    }

    private InventoryResponse toResponse(Inventory i) {
        return new InventoryResponse(
                i.getId(),
                i.getWarehouseId(),
                i.getVariantId(),
                i.getQuantity(),
                i.getReservedQuantity(),
                i.availableQuantity(),
                i.getSellerPrice(),
                i.getPurchasePrice(),
                i.getLowStockThreshold(),
                i.isLowStock());
    }

    private InventoryMovementResponse toMovementResponse(InventoryMovement m) {
        return new InventoryMovementResponse(
                m.getId(),
                m.getWarehouseId(),
                m.getVariantId(),
                m.getChange(),
                m.getType().name(),
                m.getReference(),
                m.getPerformedBy(),
                m.getCreatedAt());
    }
    // </editor-fold>
}