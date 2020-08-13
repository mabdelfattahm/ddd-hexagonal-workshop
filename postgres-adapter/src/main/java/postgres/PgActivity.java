/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package postgres;

import domain.value.AccountId;
import domain.value.Activity;
import domain.value.Money;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

/**
 * Activity entity.
 *
 * @since 1.0
 */
public class PgActivity {

    /**
     * Source account Id.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final String source;

    /**
     * Target account Id.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final String target;

    /**
     * Activity timestamp.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final Timestamp timestamp;

    /**
     * Amount of money.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final Double money;

    /**
     * Main constructor.
     *
     * @param source Source account Id.
     * @param target Target account Id,
     * @param timestamp Activity timestamp.
     * @param money Amount of money.
     * @since 1.0
     * @checkstyle ParameterNumberCheck (3 lines)
     */
    public PgActivity(
        final String source,
        final String target,
        final Timestamp timestamp,
        final Double money
    ) {
        this.source = source;
        this.target = target;
        this.timestamp = timestamp;
        this.money = money;
    }

    /**
     * Convert to domain activity.
     *
     * @return Activity domain model.
     */
    Activity toDomain() {
        return
            Activity.with(
                Optional.ofNullable(this.source).map(AccountId::with).orElse(null),
                Optional.ofNullable(this.target).map(AccountId::with).orElse(null),
                this.timestamp.toLocalDateTime(),
                Money.with(this.money)
            );
    }

    /**
     * Validate activity.
     *
     * @return Boolean.
     */
    boolean selfValidate() {
        return
            Objects.nonNull(this.timestamp)
                && Objects.nonNull(this.money)
                && (Objects.nonNull(this.source) || Objects.nonNull(this.target));
    }
}
