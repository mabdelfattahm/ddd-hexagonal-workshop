/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package postgres;

import domain.entity.Account;
import domain.value.Activity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

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
     * Store new activities.
     *
     * @param account Account.
     * @throws IllegalArgumentException If storing activities fails.
     * @since 1.0
     * @checkstyle MagicNumberCheck (11 lines)
     * @checkstyle MagicNumberCheck (11 lines)
     */
    void storeAccountNewActivities(final Account account) throws IllegalArgumentException {
        try (PreparedStatement stat = this.connection.prepareStatement(PgActivities.INSERT)) {
            final List<Activity> list = account.unsavedActivities().collect(Collectors.toList());
            for (final Activity activity : list) {
                final Instant instant = activity.timestamp.toInstant(ZoneOffset.UTC);
                stat.setString(1, activity.source.toString());
                stat.setString(2, activity.target.toString());
                stat.setTimestamp(3, Timestamp.from(instant));
                stat.setDouble(4, activity.money.value());
                stat.addBatch();
            }
            stat.executeBatch();
        } catch (final SQLException exception) {
            throw new IllegalArgumentException(exception);
        }
    }
}
