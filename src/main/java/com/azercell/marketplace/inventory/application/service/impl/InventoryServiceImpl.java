package com.azercell.marketplace.inventory.application.service.impl;

import com.azercell.marketplace.inventory.application.port.InventoryRepository;
import com.azercell.marketplace.inventory.application.service.InventoryService;
import com.azercell.marketplace.inventory.domain.aggregate.Inventory;
import com.azercell.marketplace.inventory.web.dto.response.InventoryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public void seedStock(UUID warehouseId, UUID variantId, int quantity) {
        if (quantity <= 0) return;

        var inventory = inventoryRepository.findByWarehouseAndVariant(warehouseId, variantId)
                .map(existing -> {
                    existing.restock(quantity);     // idempotent top-up if a row already exists
                    return existing;
                })
                .orElseGet(() -> Inventory.create(warehouseId, variantId, quantity));

        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public List<InventoryResponse> getByVariant(UUID variantId) {
        return inventoryRepository.findByVariant(variantId).stream()
                .map(this::toResponse)
                .toList();
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
}