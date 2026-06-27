package com.azercell.marketplace.support;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for tests that hit the database. Spins up a real PostgreSQL via Testcontainers
 * (H2 can't replicate jsonb / @ColumnTransformer / enum check-constraints used here).
 *
 * The container is static, so it starts once and is shared across every subclass.
 * {@code @ServiceConnection} auto-points spring.datasource at it — no manual URL wiring.
 */
@Testcontainers
public abstract class AbstractPostgresIntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");
}
