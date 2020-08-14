/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp.ui.accounts;

import domain.value.AccountId;
import domain.value.Activity;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Activity view model.
 *
 * @since 1.0
 */
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
public final class ActivityModel {

    /**
     * Source account.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final String source;

    /**
     * Target account.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final String target;

    /**
     * Money amount.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final String money;

    /**
     * Activity timestamp.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final String timestamp;

    /**
     * Main constructor.
     *
     * @param source Source account id.
     * @param target Target account id.
     * @param money Money amount.
     * @param timestamp Activity timestamp.
     * @checkstyle ParameterNumberCheck (3 lines)
     */
    private ActivityModel(
        final String source,
        final String target,
        final String money,
        final String timestamp
    ) {
        this.source = source;
        this.target = target;
        this.money = money;
        this.timestamp = timestamp;
    }

    /**
     * Create an activity view model from activity domain model.
     *
     * @param activity Activity domain model.
     * @return Activity view mode.
     * @since 1.0
     */
    public static ActivityModel from(final Activity activity) {
        return new ActivityModel(
            Optional.ofNullable(activity.source).map(AccountId::toString).orElse("-"),
            Optional.ofNullable(activity.target).map(AccountId::toString).orElse("-"),
            String.format("%.2f", activity.money.value()),
            activity.timestamp.format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }

    /**
     * Return a representation of an empty activity view model.
     *
     * @return Activity view model.
     * @since 1.0
     */
    public static ActivityModel empty() {
        return new ActivityModel("-", "-", "0.00", "");
    }
}
