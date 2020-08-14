/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package postgres;

import domain.entity.Account;
import domain.value.AccountId;
import domain.value.Activity;
import java.util.stream.Stream;
import port.out.LookupAccounts;
import port.out.StoreAccount;
import port.out.StoreActivity;

/**
 * Postgres adapter.
 *
 * @since 1.0
 */
public final class PgAdapter implements LookupAccounts, StoreActivity, StoreAccount {

    /**
     * Postgres config.
     */
    private final PgConfig config;

    /**
     * Main constructor.
     *
     * @param config Postgres configuration.
     */
    private PgAdapter(final PgConfig config) {
        this.config = config;
    }

    /**
     * Get an instance of the adapter.
     *
     * @return PgAdapter.
     */
    @SuppressWarnings({"PMD.ProhibitPublicStaticMethods", "PMD.AvoidDuplicateLiterals"})
    public static PgAdapter create() {
        final PgConfig config = PgConfig.create(
            "jdbc:postgresql://localhost:5432/demo",
            "postgres",
            "postgres"
        );
        return new PgAdapter(config);
    }

    @Override
    public Account byId(final AccountId id) throws IllegalArgumentException {
        return new PgAccounts(this.config.connection).findById(id);
    }

    @Override
    public Stream<Account> all() throws IllegalStateException {
        return new PgAccounts(this.config.connection).allAccounts();
    }

    @Override
    public void storeActivity(final Activity activity) throws IllegalStateException {
        new PgActivities(this.config.connection).storeActivity(activity);
    }

    @Override
    public void storeAccount(final Account account) throws IllegalStateException {
        this.config.inTransaction(
            connection -> {
                new PgAccounts(connection).save(account);
                new PgActivities(connection).storeActivity(
                    account.unsavedActivities().toArray(Activity[]::new)
                );
            }
        );
    }
}
