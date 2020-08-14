/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.in;

import domain.entity.Account;
import java.util.stream.Stream;
import port.out.LookupAccounts;

/**
 * List Accounts input port.
 *
 * @since 1.0
 */
public class ListAccounts {

    /**
     * Lookup accounts port.
     */
    private final LookupAccounts lookup;

    /**
     * Main constructor.
     *
     * @param lookup Lookup accounts port.
     * @since 1.0
     */
    public ListAccounts(final LookupAccounts lookup) {
        this.lookup = lookup;
    }

    /**
     * Accounts.
     *
     * @return Stream of accounts.
     * @since 1.0
     */
    public Stream<Account> accounts() {
        return this.lookup.all();
    }

}
