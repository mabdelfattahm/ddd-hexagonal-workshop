package postgres;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.UUID;
import java.util.function.Consumer;

public class PgConfig {

    public final Connection connection;

    private PgConfig(final String url, final String username, final String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, username, password);
    }

    static PgConfig create(final String url, final String username, final String password) throws IllegalArgumentException {
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

    public void inTransaction(final Consumer<Connection> function) throws IllegalStateException {
        try {
            this.connection.setAutoCommit(false);
            function.accept(this.connection);
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
}
