/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.out;

import domain.entity.Account;

/**
 * Update account activities.
 *
 * @since 1.0
 */
public interface UpdateAccountActivities {
    /**
     * Update account activities.
     *
     * @param account Account,
     * @throws IllegalStateException If activities failed.
     * @since 1.0
     */
    void updateActivities(Account account) throws IllegalStateException;
}
