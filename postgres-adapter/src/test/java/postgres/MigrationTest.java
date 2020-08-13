package postgres;

import domain.value.AccountId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MigrationTest {
    @Container
    private static final PostgreSQLContainer<?> CONTAINER =
        new PostgreSQLContainer<>()
            .withDatabaseName("demo")
            .withUsername("postgres")
            .withPassword("postgres");

    private static PgConfig PG_CONFIG;

    @BeforeAll
    static void initConfig() {
        MigrationTest.PG_CONFIG =
            PgConfig.create(
                MigrationTest.CONTAINER.getJdbcUrl(),
                MigrationTest.CONTAINER.getUsername(),
                MigrationTest.CONTAINER.getPassword()
            );
    }

    @Test
    void migrationTest() {
        Assertions.assertNull(
            new PgAccounts(MigrationTest.PG_CONFIG.connection)
                .findById(AccountId.create())
        );
    }
}
