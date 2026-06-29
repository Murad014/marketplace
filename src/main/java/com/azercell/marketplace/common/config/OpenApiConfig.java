package com.azercell.marketplace.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    OpenAPI marketplaceOpenAPI() {
        return new OpenAPI()
                // Relative server: Swagger "Try it out" calls the SAME origin the UI was loaded from,
                // so it works on localhost, qa and prod alike — and behind a TLS proxy (no hardcoded
                // localhost, no http/https mismatch, no cross-origin CORS).
                .servers(List.of(new Server().url("/").description("Same origin as the Swagger UI")))
                .info(new Info()
                        .title("Azercell Marketplace API")
                        .description("""
                                Internal employee marketplace — catalog (products, brands, categories),
                                inventory, financing (credit plans / installments) and orders.

                                • Send `Accept-Language: az` or `en` for localized messages.
                                • Protected endpoints require a Keycloak (RHSSO) bearer token — use the
                                  Authorize button and paste a Keycloak access token. Public storefront
                                  reads (GET /api/v1/products/**, /api/v1/credit-plans/**) need no token.""")
                        .version("v1"))
                // Apply the bearer scheme globally; endpoints permitted in SecurityConfig still work without it.
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components().addSecuritySchemes(BEARER_SCHEME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Keycloak (RHSSO) access token")));
    }
}
