/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package postgres;

import domain.value.AccountId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Migration tests.
 *
 * @since 1.0
 */
@Testcontainers
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class MigrationTest {

    /**
     * Test container.
     */
    @Container
    private static final PostgreSQLContainer<?> CONTAINER =
        new PostgreSQLContainer<>()
            .withDatabaseName("demo")
            .withUsername("postgres")
            .withPassword("postgres");

    /**
     * Postgres configuration.
     */
    private static PgConfig config;

    /**
     * Initialize configuration.
     */
    @BeforeAll
    static void initConfig() {
        MigrationTest.config =
            PgConfig.create(
                MigrationTest.CONTAINER.getJdbcUrl(),
                MigrationTest.CONTAINER.getUsername(),
                MigrationTest.CONTAINER.getPassword()
            );
    }

    @Test
    void migrationTest() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new PgAccounts(MigrationTest.config.connection).findById(AccountId.create())
        );
    }
}
