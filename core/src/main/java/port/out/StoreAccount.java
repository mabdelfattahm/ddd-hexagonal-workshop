/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.out;

import domain.entity.Account;

/**
 * Store account output port.
 *
 * @since 1.0
 */
public interface StoreAccount {

    /**
     * Store account.
     *
     * @param account Account to store.
     * @throws IllegalStateException If storing account fails.
     * @since 1.0
     */
    void storeAccount(Account account) throws IllegalStateException;
}
