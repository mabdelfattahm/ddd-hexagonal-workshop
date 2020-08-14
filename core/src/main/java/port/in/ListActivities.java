/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.in;

import domain.value.AccountId;
import domain.value.Activity;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import port.out.LookupAccounts;

/**
 * List activities input port.
 *
 * @since 1.0
 */
public final class ListActivities {

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
    public ListActivities(final LookupAccounts lookup) {
        this.lookup = lookup;
    }

    /**
     * Account activities after a certain datetime.
     *
     * @param id Account Id.
     * @param datetime Datetime.
     * @return Steam of activities.
     * @since 1.0
     */
    Stream<Activity> byAccountId(final AccountId id, final LocalDateTime datetime) {
        return this.lookup.byId(id).activities(datetime);
    }
}
