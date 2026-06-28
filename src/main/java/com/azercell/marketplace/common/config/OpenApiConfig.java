package com.azercell.marketplace.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    OpenAPI marketplaceOpenAPI() {
        return new OpenAPI()
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
