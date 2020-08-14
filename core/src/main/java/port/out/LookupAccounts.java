/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.out;

import domain.entity.Account;
import domain.value.AccountId;
import java.util.stream.Stream;

/**
 * Lookup accounts output port.
 *
 * @since 1.0
 */
public interface LookupAccounts {
    /**
     * Find account by Id.
     *
     * @param id Account id.
     * @return Account.
     * @throws IllegalArgumentException If account is not found.
     * @since 1.0
     */
    Account byId(AccountId id) throws IllegalArgumentException;

    /**
     * All accounts.
     *
     * @return Stream of accounts.
     * @throws IllegalStateException If accounts could not be listed.
     */
    Stream<Account> all() throws IllegalStateException;
}
