/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package domain.value;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Activity value object.
 *
 * @since 1.0
 */
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
public final class Activity implements Comparable<Activity> {
    /**
     * Source account.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    public final AccountId source;

    /**
     * Target account.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    public final AccountId target;

    /**
     * Money amount.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    public final Money money;

    /**
     * Activity timestamp.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    public final LocalDateTime timestamp;

    /**
     * Main constructor.
     *
     * @param source Source account. Can be null.
     * @param target Target account. Can be null.
     * @param datetime Activity timestamp.
     * @param money Amount of money.
     * @checkstyle ParameterNumberCheck (3 lines)
     */
    private Activity(
        final AccountId source,
        final AccountId target,
        final LocalDateTime datetime,
        final Money money
    ) {
        this.source = source;
        this.target = target;
        this.money = money;
        this.timestamp = datetime;
    }

    /**
     * Create a new activity.
     *
     * @param source Source account.
     * @param target Target account.
     * @param timestamp Activity timestamp.
     * @param money Money amount.
     * @return Activity.
     * @since 1.0
     * @checkstyle ParameterNumberCheck (3 lines)
     */
    public static Activity with(
        final AccountId source,
        final AccountId target,
        final LocalDateTime timestamp,
        final Money money
    ) {
        return new Activity(source, target, timestamp, money);
    }

    /**
     * Create a transfer activity.
     *
     * @param source Source account.
     * @param target Target account.
     * @param money Money amount.
     * @return Activity.
     * @since 1.0
     */
    public static Activity transfer(
        final AccountId source,
        final AccountId target,
        final Money money
    ) {
        return new Activity(source, target, LocalDateTime.now(), money);
    }

    /**
     * Create a withdrawal activity.
     *
     * @param source Source account.
     * @param money Money amount.
     * @return Activity.
     * @since 1.0
     */
    public static Activity withdraw(final AccountId source, final Money money) {
        return new Activity(source, null, LocalDateTime.now(), money);
    }

    /**
     * Create a deposition activity.
     *
     * @param target Target account.
     * @param money Money amount.
     * @return Activity.
     * @since 1.0
     */
    public static Activity deposit(final AccountId target, final Money money) {
        return new Activity(null, target, LocalDateTime.now(), money);
    }

    /**
     * Is the activity a deposition.
     *
     * @return Boolean.
     * @since 1.0
     */
    public boolean isDeposition() {
        return Objects.isNull(this.source) && Objects.nonNull(this.target);
    }

    /**
     * Is the activity a withdrawal.
     *
     * @return Boolean
     * @since 1.0
     */
    public boolean isWithdrawal() {
        return Objects.nonNull(this.source) && Objects.isNull(this.target);
    }

    /**
     * Is the activity a transaction to an account.
     *
     * @param account Account
     * @return Boolean.
     * @since 1.0
     */
    public boolean isTransferredTo(final AccountId account) {
        return Objects.nonNull(this.target) && this.target.equals(account);
    }

    /**
     * Is the activity a transaction from an account.
     *
     * @param account Account.
     * @return Boolean.
     * @since 1.0
     */
    public boolean isTransferredFrom(final AccountId account) {
        return Objects.nonNull(this.source) && this.source.equals(account);
    }

    @Override
    public int compareTo(final Activity another) {
        final long time = this.timestamp.toEpochSecond(ZoneOffset.UTC);
        return (int) (time - another.timestamp.toEpochSecond(ZoneOffset.UTC));
    }

    @Override
    @SuppressWarnings("PMD.ConfusingTernary")
    public boolean equals(final Object another) {
        final boolean result;
        if (another == this) {
            result = true;
        } else if (!(another instanceof Activity)) {
            result = false;
        } else {
            final Activity other = (Activity) another;
            result =
                Objects.equals(this.source, other.source)
                    && Objects.equals(this.target, other.target)
                    && Objects.equals(this.timestamp, other.timestamp)
                    && Objects.equals(this.money, other.money);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source, this.target, this.money, this.timestamp);
    }
}
