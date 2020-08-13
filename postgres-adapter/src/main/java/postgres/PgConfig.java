/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;
import org.flywaydb.core.Flyway;

/**
 * Postgres configuration.
 *
 * @since 1.0
 */
public final class PgConfig {

    /**
     * JDBC Connection.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    public final Connection connection;

    /**
     * Main constructor.
     *
     * @param url Postgres URL.
     * @param username Postgres username.
     * @param password Postgres password.
     * @throws SQLException If connection cannot be created.
     */
    private PgConfig(
        final String url,
        final String username,
        final String password
    ) throws SQLException {
        this.connection = DriverManager.getConnection(url, username, password);
    }

    /**
     * Execute operations in transaction mode.
     *
     * @param operation Operation to execute.
     * @throws IllegalStateException If execution fails.
     * @since 1.0
     */
    @SuppressWarnings("PMD.PreserveStackTrace")
    public void inTransaction(final Consumer<Connection> operation) throws IllegalStateException {
        try {
            this.connection.setAutoCommit(false);
            operation.accept(this.connection);
            this.connection.commit();
            this.connection.setAutoCommit(true);
        } catch (final SQLException exception) {
            try {
                this.connection.setAutoCommit(true);
                this.connection.rollback();
            } catch (final SQLException another) {
                final IllegalStateException third = new IllegalStateException(exception);
                third.addSuppressed(another);
                throw third;
            }
            throw new IllegalStateException(exception);
        }
    }

    /**
     * Create an instance of configuration.
     *
     * @param url Postgres URL.
     * @param username Postgres username.
     * @param password Postgres password.
     * @return Postgres configuration.
     * @throws IllegalArgumentException If connection parameters are invalid.
     * @since 1.0
     */
    static PgConfig create(
        final String url,
        final String username,
        final String password
    ) throws IllegalArgumentException {
        Flyway
            .configure()
            .dataSource(url, username, password)
            .schemas("demo")
            .locations("classpath:db-migrations")
            .load()
            .migrate();
        try {
            return new PgConfig(url, username, password);
        } catch (final SQLException exception) {
            throw new IllegalArgumentException("Invalid database configuration", exception);
        }
    }
}
