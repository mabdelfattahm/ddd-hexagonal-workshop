package postgres;

import domain.entity.Account;
import domain.value.AccountId;
import port.out.LookupAccounts;
import port.out.StoreAccount;
import port.out.UpdateAccountActivities;

import java.util.stream.Stream;

public class PgAdapter implements LookupAccounts, UpdateAccountActivities, StoreAccount {

    private final PgConfig config;

    public static PgAdapter getInstance() {
        final PgConfig config = PgConfig.create(
            "jdbc:postgresql://localhost:5432/demo",
            "postgres",
            "postgres"
        );
        return new PgAdapter(config);
    }

    private PgAdapter(PgConfig config) {
        this.config = config;
    }

    @Override
    public Account byId(AccountId id) throws IllegalArgumentException {
        return new PgAccounts(this.config.connection).findById(id);
    }

    @Override
    public Stream<Account> all() throws IllegalStateException {
        return new PgAccounts(this.config.connection).all();
    }

    @Override
    public void updateActivities(Account account) throws IllegalStateException {
        new PgActivities(this.config.connection).updateAccountActivities(account);
    }

    @Override
    public void store(Account account) throws IllegalStateException {
        this.config.inTransaction((connection) -> {
            new PgAccounts(connection).save(account);
            new PgActivities(connection).updateAccountActivities(account);
        });
    }
}
