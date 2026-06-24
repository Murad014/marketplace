package com.azercell.marketplace.catalog.domain.aggregate;

import com.azercell.marketplace.catalog.domain.ProductVariant;
import com.azercell.marketplace.catalog.domain.vo.*;
import com.azercell.marketplace.catalog.domain.vo.Currency;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(of = "id")
public class Product {

    private final UUID id;
    private final String sku;
    private String name;
    private UUID brandId;
    private String description;

    private Money basePrice;

    @Getter(AccessLevel.NONE)
    private Money promoPrice;

    private Currency priceCurrency;
    private Specs specs;
    private UUID categoryId;
    private final List<ProductVariant> productVariants;
    private final Set<UUID> creditPlans;
    private Availability availability;
    private Status status;


    public Product(UUID id,
                   String sku,
                   String name,
                   UUID brandId,
                   String description,
                   Money basePrice,
                   Money promoPrice,
                   Currency priceCurrency,
                   Specs specs,
                   UUID categoryId,
                   List<ProductVariant> productVariants,
                   Set<UUID> creditPlans,
                   Availability availability,
                   Status status) {
        validateName(name);
        validateBrand(brandId);
        validateBasePrice(basePrice);
        validateCategory(categoryId);
        validateProductVariants(productVariants);
        validateCreditPlans(creditPlans);
        validateAvailability(availability);

        this.id = id == null ? UUID.randomUUID() : id;
        this.name = name;
        this.brandId = brandId;
        this.description = description;
        this.basePrice = basePrice;
        this.priceCurrency = priceCurrency == null ? Currency.AZN : priceCurrency;
        this.specs = specs;
        this.categoryId = categoryId;
        this.productVariants = new ArrayList<>(productVariants);
        this.creditPlans = new HashSet<>(creditPlans);
        this.availability = availability;
        this.status = status == null ? Status.ACTIVE : status;
        this.sku = sku == null ? generateSku(name, this.id) : sku;

        applyPromoPrice(promoPrice);
    }

    public void changeName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    public void changeBrand(UUID newBrandId) {
        validateBrand(newBrandId);
        this.brandId = newBrandId;
    }

    public void changeDescription(String newDescription) {
        this.description = newDescription;
    }

    public void changeBasePrice(Money newBasePrice) {
        validateBasePrice(newBasePrice);
        if (this.promoPrice != null && this.promoPrice.isGreaterThan(newBasePrice))
            throw new IllegalStateException(
                    "New base price is lower than the current promo price; " +
                            "clear or update the promo price first.");
        this.basePrice = newBasePrice;
    }

    public void changePriceCurrency(Currency newPriceCurrency) {
        validateCurrency(newPriceCurrency);
        this.priceCurrency = newPriceCurrency;
    }

    public void changeSpecs(Specs newSpecs) {
        // Specs are optional, consistently with construction.
        this.specs = newSpecs;
    }

    public void changeCategory(UUID newCategoryId) {
        validateCategory(newCategoryId);
        this.categoryId = newCategoryId;
    }

    public void applyPromoPrice(Money newPromoPrice) {
        if (newPromoPrice == null)
            return;
        if (newPromoPrice.isGreaterThan(this.basePrice))
            throw new IllegalStateException("Promo price cannot be greater than base price.");
        this.promoPrice = newPromoPrice;
    }

    public void clearPromoPrice() {
        this.promoPrice = null;
    }

    public Optional<Money> getPromoPrice() {
        return Optional.ofNullable(promoPrice);
    }

    public Money getSellingPrice() {
        return promoPrice != null ? promoPrice : basePrice;
    }

    public void addProductVariant(ProductVariant productVariant) {
        validateNoDuplicateVariant(productVariant);
        this.productVariants.add(productVariant);
    }

    public void removeProductVariant(UUID variantId) {
        boolean present = productVariants.stream()
                .anyMatch(v -> v.getId().equals(variantId));
        if (present && productVariants.size() == 1)
            throw new IllegalStateException("A product must keep at least one product variant.");
        productVariants.removeIf(v -> v.getId().equals(variantId));
    }

    public void addCreditPlan(UUID creditPlanId) {
        if (creditPlanId == null)
            throw new IllegalArgumentException("Credit plan id cannot be null.");
        creditPlans.add(creditPlanId);
    }

    public void removeCreditPlan(UUID creditPlanId) {
        if (creditPlans.contains(creditPlanId) && creditPlans.size() == 1)
            throw new IllegalStateException("A product must keep at least one payment option.");
        creditPlans.remove(creditPlanId);
    }

    public void changeAvailability(Availability newAvailability) {
        validateAvailability(newAvailability);
        this.availability = newAvailability;
    }

    public void activate() {
        this.status = Status.ACTIVE;
    }

    public void deactivate() {
        this.status = Status.IN_ACTIVE;
    }

    public List<ProductVariant> getProductVariants() {
        return List.copyOf(productVariants);
    }

    public Set<UUID> getCreditPlans() {
        return Set.copyOf(creditPlans);
    }


    /**
     * Reconciles the variant list against what's incoming:
     * - incoming variant whose id matches an existing one  -> update it
     * - incoming variant with null id (or unmatched id)    -> add it
     * - existing variant not present in incoming           -> remove it
     * Everything goes through the root, so the aggregate boundary holds.
     */
    public void syncVariants(List<ProductVariant> incoming) {
        if (incoming == null || incoming.isEmpty())
            throw new DomainException(ErrorCode.PRODUCT_VARIANT_REQUIRED);

        // 1. remove variants that are no longer present
        Set<UUID> incomingIds = incoming.stream()
                .map(ProductVariant::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        this.productVariants.removeIf(existing -> !incomingIds.contains(existing.getId()));

        // 2. add new / update existing
        for (ProductVariant in : incoming) {
            if (in.getId() == null) {
                addProductVariant(in);                 // new -> goes through existing add (dup check runs)
            } else {
                ProductVariant existing = findVariant(in.getId());
                if (existing == null) {
                    addProductVariant(in);             // id given but not found -> treat as new
                } else {
                    existing.changeColor(in.getColor());
                    existing.overridePrice(in.getPriceOverride());
                    existing.changeSpecs(in.getSpecs());
                    existing.syncImages(in.getProductImages());   // see step 2
                }
            }
        }
    }


    // <editor-fold desc="privateHelperMethods">
    private ProductVariant findVariant(UUID variantId) {
        return productVariants.stream()
                .filter(v -> v.getId().equals(variantId))
                .findFirst()
                .orElse(null);
    }

    private static String generateSku(String name, UUID id) {
        return name.replace(" ", "-").toUpperCase()
                + "-"
                + id.toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private void validateNoDuplicateVariant(ProductVariant productVariant) {
        if (productVariant == null)
            throw new IllegalArgumentException("Product variant cannot be null.");
        UUID newId = productVariant.getId();
        String newSku = productVariant.getSku();
        boolean duplicate = productVariants.stream()
                .anyMatch(v -> v.getId().equals(newId) || v.getSku().equals(newSku));
        if (duplicate)
            throw new IllegalStateException("A product variant with the same id or SKU already exists.");
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank())
            throw new DomainException(ErrorCode.PRODUCT_NAME_REQUIRED);
    }

    private static void validateBrand(UUID brandId) {
        if (brandId == null)
            throw new IllegalArgumentException("Product brand cannot be null.");
    }

    private static void validateBasePrice(Money basePrice) {
        if (basePrice == null)
            throw new IllegalArgumentException("Product base price cannot be null.");
    }

    private static void validateCategory(UUID categoryId) {
        if (categoryId == null)
            throw new IllegalArgumentException("Category id cannot be null.");
    }

    private static void validateProductVariants(List<ProductVariant> productVariants) {
        if (productVariants == null || productVariants.isEmpty())
            throw new IllegalArgumentException("Every product must have at least one product variant.");
    }

    private static void validateCreditPlans(Set<UUID> creditPlans) {
        if (creditPlans == null || creditPlans.isEmpty())
            throw new IllegalArgumentException("Every product must have at least one payment option.");
    }

    private static void validateAvailability(Availability availability) {
        if (availability == null)
            throw new IllegalArgumentException("Availability must be selected for every product.");
    }

    private static void validateCurrency(Currency currency) {
        if (currency == null)
            throw new IllegalArgumentException("Product price currency cannot be null.");
    }
    // </editor-fold>
}