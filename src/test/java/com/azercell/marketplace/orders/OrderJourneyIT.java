package com.azercell.marketplace.orders;

import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.BrandJpaEntity;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.CategoryJpaEntity;
import com.azercell.marketplace.catalog.infrastructure.persistence.repository.BrandJpaRepository;
import com.azercell.marketplace.catalog.infrastructure.persistence.repository.CategoryJpaRepository;
import com.azercell.marketplace.support.AbstractPostgresIntegrationTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Reference TIER 3 — behaviour / end-to-end through HTTP. Exercises the real cross-context wiring:
 * create product (event-seeds stock) -> place order (reserves stock through the InventoryApi ACL).
 * Few of these; they double as living documentation of the money path.
 */
@SpringBootTest
@AutoConfigureMockMvc
class OrderJourneyIT extends AbstractPostgresIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BrandJpaRepository brandRepository;
    @Autowired
    private CategoryJpaRepository categoryRepository;

    @Test
    void placingAnOrder_reservesStock_inThePrimaryWarehouse() throws Exception {
        var brandId = seedBrand();
        var categoryId = seedCategory();

        // 1. warehouse + make it primary (the stock-seeding / fulfilment target)
        var warehouseJson = postJson("/api/v1/admin/warehouses",
                "{\"name\":\"Baku IT\",\"code\":\"BAKU-IT\",\"location\":\"Baku\"}");
        String warehouseId = JsonPath.read(warehouseJson, "$.data.id");
        mvc.perform(patch("/api/v1/admin/warehouses/{id}/primary", warehouseId))
                .andExpect(status().isOk());

        // 2. product with stock 5 -> ProductCreatedEvent seeds inventory into the primary warehouse
        var productJson = postJson("/api/v1/admin/products", """
                {"categoryId":"%s","name":"IT Phone","brandId":"%s","price":1000,"availability":"ORDER_NOW",
                 "productVariants":[{"colorName":"Red","colorHexCode":"#FF0000","stockCount":5,
                   "images":[{"name":"f","url":"https://x/f.png","altText":"f","isPrimary":true,"sort":0}]}]}
                """.formatted(categoryId, brandId));
        String productId = JsonPath.read(productJson, "$.data.id");

        // 3. resolve the variant id from the product detail
        var detail = mvc.perform(get("/api/v1/products/{id}", productId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String variantId = JsonPath.read(detail, "$.data.variants[0].id");

        // 4. place an order for 2 units
        mvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content("""
                        {"userId":"11111111-1111-1111-1111-111111111111",
                         "items":[{"variantId":"%s","quantity":2}]}
                        """.formatted(variantId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("PENDING"));

        // 5. stock is reserved: 5 on hand, 2 reserved, 3 available
        var inventory = mvc.perform(get("/api/v1/admin/inventory").param("variantId", variantId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int reserved = JsonPath.read(inventory, "$.data[0].reservedQuantity");
        int available = JsonPath.read(inventory, "$.data[0].availableQuantity");
        assertThat(reserved).isEqualTo(2);
        assertThat(available).isEqualTo(3);
    }

    private String postJson(String url, String body) throws Exception {
        return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
    }

    private UUID seedBrand() {
        var b = new BrandJpaEntity();
        b.setId(UUID.randomUUID());
        b.setName("Apple IT");
        b.setCode("APPLIT");
        b.setStatus(Status.ACTIVE);
        return brandRepository.save(b).getId();
    }

    private UUID seedCategory() {
        var c = new CategoryJpaEntity();
        c.setId(UUID.randomUUID());
        c.setName("Electronics");
        c.setSlug("electronics-it");
        c.setStatus(Status.ACTIVE);
        return categoryRepository.save(c).getId();
    }
}
