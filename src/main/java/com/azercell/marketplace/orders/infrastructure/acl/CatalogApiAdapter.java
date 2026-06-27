package com.azercell.marketplace.orders.infrastructure.acl;

import com.azercell.marketplace.catalog.application.port.ProductRepository;
import com.azercell.marketplace.catalog.domain.aggregate.Product;
import com.azercell.marketplace.catalog.domain.ProductVariant;
import com.azercell.marketplace.orders.application.port.CatalogApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Translates catalog data into the order-line snapshot orders needs. Pricing is taken from the
 * product: originalPrice = base price, unitPrice = selling price (promo-aware).
 */
@Component
@RequiredArgsConstructor
public class CatalogApiAdapter implements CatalogApi {

    private final ProductRepository productRepository;

    @Override
    public Optional<OrderLineInfo> findOrderLineInfo(UUID variantId) {
        return productRepository.findByVariantId(variantId)
                .flatMap(product -> product.getProductVariants().stream()
                        .filter(v -> v.getId().equals(variantId))
                        .findFirst()
                        .map(variant -> toInfo(product, variant)));
    }

    private OrderLineInfo toInfo(Product product, ProductVariant variant) {
        var colorName = variant.getColor() != null ? variant.getColor().getName() : null;
        var originalPrice = product.getBasePrice().amount();
        var unitPrice = product.getSellingPrice().amount();
        var wasPromo = product.getPromoPrice().isPresent();
        return new OrderLineInfo(
                variant.getId(),
                product.getName(),
                variant.getSku(),
                colorName,
                originalPrice,
                unitPrice,
                wasPromo,
                null);     // promo label: no variant_promos context yet
    }
}