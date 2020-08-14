/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.in;

import domain.entity.Account;
import domain.value.AccountId;
import domain.value.Money;
import port.out.StoreAccount;

/**
 * Create account input port.
 *
 * @since 1.0
 */
public final class CreateAccount {

    /**
     * Store account output port.
     */
    private final StoreAccount storage;

    /**
     * Main constructor.
     *
     * @param storage Store account output port.
     * @since 1.0
     */
    public CreateAccount(final StoreAccount storage) {
        this.storage = storage;
    }

    /**
     * Create account.
     *
     * @param starting Starting balance.
     * @return Account.
     * @throws IllegalStateException If storing account fails.
     * @since 1.0
     */
    public Account create(final Money starting) throws IllegalStateException {
        final Account account = Account.with(AccountId.create(), starting);
        this.storage.store(account);
        return account;
    }
}
