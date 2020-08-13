package postgres;

import domain.entity.Account;
import domain.value.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

public class PgActivities {

    static final String TABLE_NAME = "demo.activities";

    private final Connection connection;

    private static final String INSERT =
        String.join(
            " ",
            "INSERT INTO",
            PgActivities.TABLE_NAME,
            "(source_account, target_account, time_stamp, money) VALUES (?, ?, ?, ?)"
        );


    public PgActivities(Connection connection) {
        this.connection = connection;
    }

    void updateAccountActivities(Account account) throws IllegalArgumentException {
        try (final PreparedStatement stat = this.connection.prepareStatement(PgActivities.INSERT)) {
            for (Activity activity : account.unsavedActivities().collect(Collectors.toList())) {
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
