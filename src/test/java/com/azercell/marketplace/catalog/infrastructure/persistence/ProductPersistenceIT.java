package com.azercell.marketplace.catalog.infrastructure.persistence;

import com.azercell.marketplace.catalog.application.port.ProductRepository;
import com.azercell.marketplace.catalog.domain.Color;
import com.azercell.marketplace.catalog.domain.ProductImage;
import com.azercell.marketplace.catalog.domain.ProductVariant;
import com.azercell.marketplace.catalog.domain.aggregate.Brand;
import com.azercell.marketplace.catalog.domain.aggregate.Product;
import com.azercell.marketplace.catalog.domain.vo.Availability;
import com.azercell.marketplace.catalog.domain.vo.Currency;
import com.azercell.marketplace.catalog.domain.vo.HexCode;
import com.azercell.marketplace.catalog.domain.vo.Money;
import com.azercell.marketplace.catalog.domain.vo.Specs;
import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.BrandJpaEntity;
import com.azercell.marketplace.catalog.infrastructure.persistence.repository.adapter.ProductJpaRepositoryAdapter;
import com.azercell.marketplace.common.config.AuditAwareImplConfig;
import com.azercell.marketplace.support.AbstractPostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Reference TIER 2 — persistence round-trip. @DataJpaTest loads only JPA; @Import pulls in the
 * adapter under test. Real Postgres (via the base class) so jsonb actually round-trips.
 *
 * This is the test class that would have caught the image name/url swap and the dropped promoPrice.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ProductJpaRepositoryAdapter.class, AuditAwareImplConfig.class})
class ProductPersistenceIT extends AbstractPostgresIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void roundTrips_product_with_variant_images_promo_and_jsonbSpecs() {
        // Brand row must exist (products.brand_id FK).
        var brandId = UUID.randomUUID();
        var brandEntity = new BrandJpaEntity();
        brandEntity.setId(brandId);
        brandEntity.setName("Apple");
        brandEntity.setCode("APPL");
        brandEntity.setStatus(Status.ACTIVE);
        em.persist(brandEntity);
        em.flush();

        var brand = Brand.rehydrate(brandId, "Apple", "APPL", Status.ACTIVE);

        var color = Color.create("Red", new HexCode("#FF0000"));
        var image = ProductImage.create("https://cdn/front.png", "front", "Front view", true, 0);
        var variant = ProductVariant.create(color, new Specs("{\"RAM\":\"8GB\"}"), List.of(image));

        var product = new Product(
                null, null, "iPhone 15", brandId, "A phone",
                Money.of(new BigDecimal("1000")), Money.of(new BigDecimal("800")),
                Currency.AZN, new Specs("{\"display\":\"OLED\"}"),
                UUID.randomUUID(), List.of(variant),
                Set.of(UUID.randomUUID()), Availability.ORDER_NOW, Status.ACTIVE);

        var id = productRepository.insert(product, brand);
        em.flush();
        em.clear(); // force a real reload from the DB, not the first-level cache

        var reloaded = productRepository.findById(id).orElseThrow();

        assertThat(reloaded.getName()).isEqualTo("iPhone 15");
        assertThat(reloaded.getBasePrice().amount()).isEqualByComparingTo("1000.00");
        assertThat(reloaded.getPromoPrice())
                .hasValueSatisfying(m -> assertThat(m.amount()).isEqualByComparingTo("800.00"));
        assertThat(reloaded.getSpecs().json()).contains("OLED"); // jsonb survived the trip

        var v = reloaded.getProductVariants().get(0);
        assertThat(v.getSpecs().json()).contains("RAM");

        var img = v.getProductImages().get(0);
        assertThat(img.getName()).isEqualTo("front");                 // not swapped
        assertThat(img.getUrl()).isEqualTo("https://cdn/front.png");  // not swapped
        assertThat(img.isPrimary()).isTrue();
    }
}
