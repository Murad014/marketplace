package com.azercell.marketplace.catalog.infrastructure.persistence.repository.spec;

import com.azercell.marketplace.catalog.application.port.ProductFilter;
import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.ProductJpaEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Builds the dynamic predicate for the active-product listing from a {@link ProductFilter}. */
public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<ProductJpaEntity> activeMatching(ProductFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always restrict to ACTIVE products.
            predicates.add(cb.equal(root.get("status"), Status.ACTIVE));

            if (filter != null) {
                if (hasText(filter.name())) {
                    predicates.add(cb.like(cb.lower(root.get("name")),
                            "%" + filter.name().trim().toLowerCase() + "%"));
                }
                if (filter.categoryId() != null) {
                    predicates.add(cb.equal(root.get("categoryId"), filter.categoryId()));
                }

                // Effective selling price = COALESCE(promo_price, base_price).
                if (filter.minPrice() != null || filter.maxPrice() != null) {
                    Expression<BigDecimal> sellingPrice =
                            cb.coalesce(root.get("promoPrice"), root.get("basePrice"));
                    if (filter.minPrice() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(sellingPrice, filter.minPrice()));
                    }
                    if (filter.maxPrice() != null) {
                        predicates.add(cb.lessThanOrEqualTo(sellingPrice, filter.maxPrice()));
                    }
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String s) {
        return s != null && !s.isBlank();
    }
}
