/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package domain.value;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Activity window domain model.
 *
 * @since 1.0
 */
@SuppressWarnings({"PMD.ProhibitPublicStaticMethods", "PMD.TooManyMethods"})
public final class ActivityWindow {

    /**
     * When the activity window was loaded/created.
     */
    private final LocalDateTime loaded;

    /**
     * Activities map.
     */
    private final Map<AccountId, List<Activity>> activities;

    /**
     * Main constructor.
     *
     * @param timestamp Load time.
     * @param activities Map of activities.
     */
    private ActivityWindow(
        final LocalDateTime timestamp,
        final Map<AccountId, List<Activity>> activities
    ) {
        this.loaded = timestamp;
        this.activities = activities;
    }

    /**
     * Create a new activity window.
     *
     * @return Activity window.
     * @since 1.0
     */
    public static ActivityWindow create() {
        return new ActivityWindow(LocalDateTime.now(), new HashMap<>());
    }

    /**
     * Activity window with an activity list.
     *
     * @param time Load time.
     * @param activities List of activities.
     * @return Activity window.
     * @since 1.0
     */
    public static ActivityWindow with(
        final LocalDateTime time,
        final List<Activity> activities
    ) {
        final ActivityWindow window = new ActivityWindow(time, new HashMap<>());
        activities.forEach(window::addActivity);
        return window;
    }

    /**
     * Activity window that you cannot add new activities to.
     *
     * @param activities List of activities.
     * @return Activity window.
     * @since 1.0
     */
    public static ActivityWindow unmodifiable(final List<Activity> activities) {
        final ActivityWindow window = new ActivityWindow(LocalDateTime.now(), new HashMap<>());
        activities.forEach(window::addActivity);
        return new ActivityWindow(window.loaded, Collections.unmodifiableMap(window.activities));
    }

    /**
     * Amount withdrawn from an account within this activity window.
     *
     * @param account Account Id.
     * @return Amount of money withdrawn from account.
     * @since 1.0
     */
    public Money withdrawnFromAccount(final AccountId account) {
        return this.withdrawnFromAccount(account, this.loaded);
    }

    /**
     * Amount deposited into an account within this activity window.
     *
     * @param account Account Id.
     * @return Amount of money deposited into account.
     * @since 1.0
     */
    public Money depositedIntoAccount(final AccountId account) {
        return this.depositedIntoAccount(account, this.loaded);
    }

    /**
     * Amount withdrawn from an account after the given datetime.
     *
     * @param account Account Id.
     * @param datetime Datetime.
     * @return Amount of money withdrawn from account.
     * @since 1.0
     */
    public Money withdrawnFromAccount(final AccountId account, final LocalDateTime datetime) {
        return
            this
                .activities
                .get(account)
                .stream()
                .filter(activity -> ActivityWindow.isWithdrawal(account, activity, datetime))
                .map(activity -> activity.money)
                .reduce(Money::plus)
                .orElse(Money.with(0));
    }

    /**
     * Amount deposited from into an account after the given datetime.
     *
     * @param account Account Id.
     * @param datetime Datetime.
     * @return Amount of money deposited into account.
     * @since 1.0
     */
    public Money depositedIntoAccount(final AccountId account, final LocalDateTime datetime) {
        return
            this
                .activities
                .get(account)
                .stream()
                .filter(activity -> ActivityWindow.isDeposition(account, activity, datetime))
                .map(activity -> activity.money)
                .reduce(Money::plus)
                .orElse(Money.with(0));
    }

    /**
     * Add activity to this window.
     *
     * @param activity Activity.
     * @since 1.0
     */
    public void addActivity(final Activity activity) {
        Optional.ofNullable(activity.source).ifPresent(acc -> this.insertActivity(acc, activity));
        Optional.ofNullable(activity.target).ifPresent(acc -> this.insertActivity(acc, activity));
    }

    /**
     * All activities inside this window.
     *
     * @return Stream of activities.
     * @since 1.0
     */
    public Stream<Activity> all() {
        return this.activities.values().stream().flatMap(Collection::stream);
    }

    /**
     * Activities within the window that happened after a certain datetime.
     *
     * @param datetime Datetime.
     * @return Stream of activities.
     * @since 1.0
     */
    public Stream<Activity> filtered(final LocalDateTime datetime) {
        return this.all().filter(activity -> !activity.timestamp.isBefore(datetime));
    }

    /**
     * Only activities after this window was created.
     *
     * @return Stream of activities.
     * @since 1.0
     */
    public Stream<Activity> newlyAdded() {
        return this.all().filter(activity -> !activity.timestamp.isBefore(this.loaded));
    }

    /**
     * Insert activity into activity map.
     *
     * @param account Account Id.
     * @param activity Activity.
     */
    private void insertActivity(final AccountId account, final Activity activity) {
        final List<Activity> list = this.activities.getOrDefault(account, new LinkedList<>());
        list.add(activity);
        this.activities.put(account, list);
    }

    /**
     * Is the given activity a deposit into the given account after the given datetime.
     *
     * @param account Account Id.
     * @param activity Activity.
     * @param datetime Datetime.
     * @return Boolean.
     */
    private static boolean isDeposition(
        final AccountId account,
        final Activity activity,
        final LocalDateTime datetime
    ) {
        return !activity.timestamp.isBefore(datetime)
            && (activity.isDeposition() || activity.isTransferredTo(account));
    }

    /**
     * Is the given activity a withdrawal from the given account after the given datetime.
     *
     * @param account Account Id.
     * @param activity Activity.
     * @param datetime Datetime.
     * @return Boolean.
     */
    private static boolean isWithdrawal(
        final AccountId account,
        final Activity activity,
        final LocalDateTime datetime
    ) {
        return !activity.timestamp.isBefore(datetime)
            && (activity.isWithdrawal() || activity.isTransferredFrom(account));
    }
}
