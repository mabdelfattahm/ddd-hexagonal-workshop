/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.out;

import domain.value.Activity;

/**
 * Store account activity.
 *
 * @since 1.0
 */
public interface StoreActivity {
    /**
     * Store account activity.
     *
     * @param activity Activity,
     * @throws IllegalStateException If storing activity failed.
     * @since 1.0
     */
    void storeActivity(Activity activity) throws IllegalStateException;
}
