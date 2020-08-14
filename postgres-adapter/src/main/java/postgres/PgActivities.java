/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package postgres;

import domain.value.AccountId;
import domain.value.Activity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * Activities repository.
 *
 * @since 1.0
 */
public final class PgActivities {

    /**
     * Table name.
     */
    static final String TABLE_NAME = "demo.activities";

    /**
     * Insert query.
     */
    private static final String INSERT =
        String.join(
            " ",
            "INSERT INTO",
            PgActivities.TABLE_NAME,
            "(source_account, target_account, time_stamp, money) VALUES (?, ?, ?, ?)"
        );

    /**
     * JDBC connection.
     */
    private final Connection connection;

    /**
     * Main constructor.
     *
     * @param connection JDBC connection.
     * @since 1.0
     */
    public PgActivities(final Connection connection) {
        this.connection = connection;
    }

    /**
     * Store activities.
     *
     * @param activities Activities.
     * @throws IllegalArgumentException If storing activities fails.
     * @since 1.0
     */
    void storeActivity(final Activity... activities) throws IllegalArgumentException {
        try (PreparedStatement stat = this.connection.prepareStatement(PgActivities.INSERT)) {
            for (final Activity activity : activities) {
                PgActivities.addActivityToBatch(stat, activity);
            }
            stat.executeBatch();
        } catch (final SQLException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    /**
     * Add activity to statement to execute in batch mode.
     *
     * @param stat Prepared statement.
     * @param activity Activity.
     * @throws SQLException If adding element to statement fails.
     * @since 1.0
     * @checkstyle MagicNumberCheck (13 lines)
     * @checkstyle MagicNumberCheck (13 lines)
     */
    private static void addActivityToBatch(
        final PreparedStatement stat,
        final Activity activity
    ) throws SQLException {
        final Instant instant = activity.timestamp.toInstant(ZoneOffset.UTC);
        final String source = PgActivities.stringFromAccountId(activity.source);
        final String target = PgActivities.stringFromAccountId(activity.target);
        stat.setString(1, source);
        stat.setString(2, target);
        stat.setTimestamp(3, Timestamp.from(instant));
        stat.setDouble(4, activity.money.value());
        stat.addBatch();
    }

    /**
     * Get string from account Id.
     *
     * @param account Account Id.
     * @return String
     * @since 1.0
     */
    private static String stringFromAccountId(final AccountId account) {
        return Optional.ofNullable(account).map(AccountId::toString).orElse(null);
    }
}
