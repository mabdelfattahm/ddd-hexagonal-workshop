package postgres;

import common.PostConditions;
import domain.entity.Account;
import domain.value.AccountId;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PgAccounts {

    static final String TABLE_NAME = "demo.accounts";

    private final Connection connection;

    private final static String ALL =
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

    private final static String BY_ID =
        String.format("%s WHERE account_id = ?", PgAccounts.ALL);

    private final static String INSERT =
        String.join(
            " ",
            "INSERT INTO",
            PgAccounts.TABLE_NAME,
            "(account_id, start_balance)",
            "VALUES (?, ?)",
            "ON CONFLICT DO UPDATE SET start_balance = ?"
        );

    public PgAccounts(Connection connection) {
        this.connection = connection;
    }

    void save(Account account) throws IllegalStateException {
        try (final PreparedStatement stat = this.connection.prepareStatement(PgAccounts.INSERT)) {
            stat.setString(1, account.accountId().toString());
            stat.setDouble(2, account.balance().value());
            stat.setDouble(3, account.balance().value());
        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }

    Account findById(AccountId id) throws IllegalArgumentException {
        try (final PreparedStatement stat = this.connection.prepareStatement(PgAccounts.BY_ID)) {
            stat.setString(1, id.toString());
            final ResultSet result = stat.executeQuery();
            final List<PgAccount> accounts =
                PgAccounts.listFromStream(
                    PgAccounts.streamFromResultSet(result)
                );
            PgAccounts.checkAccountsList(
                accounts,
                String.format("Multiple accounts found with id %s", id.toString())
            );
            return accounts.get(0).toDomain();
        } catch (SQLException exception) {
            throw new IllegalArgumentException(
                String.format("Account with an id %s not found", id.toString())
            );
        }
    }

    Stream<Account> all() throws IllegalStateException {
        try (final Statement stat = this.connection.createStatement()) {
            final ResultSet result = stat.executeQuery(PgAccounts.ALL);
            return
                PgAccounts
                    .listFromStream(PgAccounts.streamFromResultSet(result))
                    .stream()
                    .map(PgAccount::toDomain);
        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }

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

    private static Stream<PgAccount> streamFromResultSet(final ResultSet result) throws SQLException {
        final Stream.Builder<PgAccount> builder = Stream.builder();
        while (result.next()) {
            builder.accept(
                new PgAccount(
                    result.getString("account_id"),
                    result.getDouble("start_balance"),
                    List.of(
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

    private static void checkAccountsList(List<PgAccount> accounts, String error) throws IllegalArgumentException {
        PostConditions.must(
            accounts.size(),
            size -> size == 1,
            () -> new IllegalArgumentException(error)
        );
    }
}
