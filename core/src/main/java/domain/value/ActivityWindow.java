package domain.value;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ActivityWindow {

    private final LocalDateTime loaded;
    private final List<Activity> activities;

    private ActivityWindow(LocalDateTime timestamp, List<Activity> activities) {
        this.loaded = timestamp;
        this.activities = activities;
    }

    public static ActivityWindow create() {
        return new ActivityWindow(LocalDateTime.now(), new LinkedList<>());
    }

    public static ActivityWindow with(LocalDateTime time, List<Activity> activities) {
        return new ActivityWindow(time, new LinkedList<>(activities));
    }

    public static ActivityWindow unmodifiable(List<Activity> activities) {
        return new ActivityWindow(LocalDateTime.now(), List.copyOf(activities));
    }

    public Money withdrawnFromAccount(AccountId account) {
        return this.withdrawnFromAccount(account, this.loaded);
    }

    public Money depositedIntoAccount(AccountId account) {
        return this.depositedIntoAccount(account, this.loaded);
    }

    public Money withdrawnFromAccount(AccountId account, LocalDateTime since) {
        return
            this
                .activities
                .stream()
                .filter(activity -> Objects.nonNull(activity.source))
                .filter(activity -> activity.source.equals(account))
                .dropWhile(activity -> activity.timestamp.isBefore(since))
                .map(activity -> activity.money)
                .reduce(Money::plus)
                .orElse(Money.of(0));
    }

    public Money depositedIntoAccount(AccountId account,  LocalDateTime since) {
        return
            this
                .activities
                .stream()
                .filter(activity -> Objects.nonNull(activity.target))
                .filter(activity -> activity.target.equals(account))
                .dropWhile(activity -> activity.timestamp.isBefore(since))
                .map(activity -> activity.money)
                .reduce(Money::plus)
                .orElse(Money.of(0));
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public Stream<Activity> all() {
        return this.activities.stream();
    }

    public Stream<Activity> filtered(final LocalDateTime since) {
        return this.all().dropWhile(activity -> activity.timestamp.isBefore(since));
    }

    public Stream<Activity> newlyAdded() {
        return this.all().dropWhile(activity -> activity.timestamp.isBefore(this.loaded));
    }
}
