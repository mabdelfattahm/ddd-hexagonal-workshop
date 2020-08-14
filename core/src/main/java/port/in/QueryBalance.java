/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.in;

import domain.value.AccountId;
import domain.value.Money;
import port.out.LookupAccounts;

/**
 * Query balance port.
 *
 * @since 1.0
 */
public final class QueryBalance {

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
    public QueryBalance(final LookupAccounts lookup) {
        this.lookup = lookup;
    }

    /**
     * Get account balance.
     *
     * @param id Account Id.
     * @return Balance.
     * @since 1.0
     */
    public Money getAccountBalance(final AccountId id) {
        return this.lookup.byId(id).balance();
    }
}
