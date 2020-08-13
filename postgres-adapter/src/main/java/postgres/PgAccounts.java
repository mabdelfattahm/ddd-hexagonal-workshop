/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package postgres;

import common.PostConditions;
import domain.entity.Account;
import domain.value.AccountId;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Accounts repository.
 *
 * @since 1.0
 */
public final class PgAccounts {

    /**
     * Table name.
     *
     * @since 1.0
     */
    static final String TABLE_NAME = "demo.accounts";

    /**
     * Select all query.
     */
    private static final String ALL =
        String.join(
            " ",
            "SELECT",
            "account_id, start_balance, source_account, target_account, time_stamp, money",
            "FROM",
            PgAccounts.TABLE_NAME,
            "LEFT OUTER JOIN",
            PgActivities.TABLE_NAME,
            "ON account_id = source_account OR account_id = target_account"
        );

    /**
     * Select by Id query.
     */
    private static final String BY_ID =
        String.format("%s WHERE account_id = ?", PgAccounts.ALL);

    /**
     * Insertion query.
     */
    private static final String INSERT =
        String.join(
            " ",
            "INSERT INTO",
            PgAccounts.TABLE_NAME,
            "(account_id, start_balance)",
            "VALUES (?, ?)"
        );

    /**
     * Connection.
     */
    private final Connection connection;

    /**
     * Main constructor.
     *
     * @param connection JDBC connection.
     * @since 1.0
     */
    public PgAccounts(final Connection connection) {
        this.connection = connection;
    }

    /**
     * Save account to database.
     *
     * @param account Account.
     * @throws IllegalStateException If saving fails.
     * @since 1.0
     */
    void save(final Account account) throws IllegalStateException {
        try (PreparedStatement stat = this.connection.prepareStatement(PgAccounts.INSERT)) {
            stat.setString(1, account.accountId().toString());
            stat.setDouble(2, account.balance().value());
            stat.execute();
        } catch (final SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }

    /**
     * Find an account by Id.
     *
     * @param id Account id.
     * @return Account
     * @throws IllegalArgumentException If account id is not found.
     * @since 1.0
     */
    Account findById(final AccountId id) throws IllegalArgumentException {
        try (PreparedStatement stat = this.connection.prepareStatement(PgAccounts.BY_ID)) {
            stat.setString(1, id.toString());
            try (ResultSet result = stat.executeQuery()) {
                final List<PgAccount> accounts =
                    PgAccounts.listFromStream(
                        PgAccounts.streamFromResultSet(result)
                    );
                PgAccounts.checkAccountsList(
                    accounts,
                    String.format("Multiple accounts found with id %s", id.toString())
                );
                return accounts.get(0).toDomain();
            }
        } catch (final SQLException exception) {
            throw new IllegalArgumentException(
                String.format("Account with an id %s not found", id.toString()), exception
            );
        }
    }

    /**
     * Retrieve all accounts.
     *
     * @return Stream of accounts.
     * @throws IllegalStateException If accounts retrieval fails.
     * @since 1.0
     */
    Stream<Account> allAccounts() throws IllegalStateException {
        try (Statement stat = this.connection.createStatement()) {
            try (ResultSet result =  stat.executeQuery(PgAccounts.ALL)) {
                return
                    PgAccounts
                        .listFromStream(PgAccounts.streamFromResultSet(result))
                        .stream()
                        .map(PgAccount::toDomain);
            }
        } catch (final SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }

    /**
     * Check if account list contains exactly only one element.
     *
     * @param accounts Accounts list.
     * @param error Error message.
     * @throws IllegalArgumentException If the accounts list does not contain exactly one element.
     */
    private static void checkAccountsList(
        final List<PgAccount> accounts,
        final String error
    ) throws IllegalArgumentException {
        PostConditions.must(
            accounts.size(),
            size -> size == 1,
            () -> new IllegalArgumentException(error)
        );
    }

    /**
     * Convert JDBC result set to a stream of Pg accounts.
     *
     * @param result Result set.
     * @return Stream of Pg accounts.
     * @throws SQLException If result set access fails.
     */
    private static Stream<PgAccount> streamFromResultSet(
        final ResultSet result
    ) throws SQLException {
        final Stream.Builder<PgAccount> builder = Stream.builder();
        while (result.next()) {
            builder.accept(
                new PgAccount(
                    result.getString("account_id"),
                    result.getDouble("start_balance"),
                    Collections.singletonList(
                        new PgActivity(
                            result.getString("source_account"),
                            result.getString("target_account"),
                            result.getTimestamp("time_stamp"),
                            result.getDouble("money")
                        )
                    )
                )
            );
        }
        return builder.build();
    }

    /**
     * Collect a stream of single activity accounts into a list of accounts with its activities.
     *
     * @param stream A stream of Pg accounts.
     * @return A list of Pg accounts.
     */
    private static List<PgAccount> listFromStream(final Stream<PgAccount> stream) {
        return
            new ArrayList<>(
                stream
                    .collect(
                        Collectors.toMap(
                            acc -> acc.id,
                            Function.identity(),
                            (acc, other) -> {
                                acc.activities.addAll(other.activities);
                                return acc;
                            }
                        )
                    )
                    .values()
            );
    }
}
