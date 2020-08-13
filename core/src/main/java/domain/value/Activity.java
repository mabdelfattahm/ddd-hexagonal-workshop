package domain.value;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Activity implements Comparable<Activity> {
    public final AccountId source;
    public final AccountId target;
    public final Money money;
    public final LocalDateTime timestamp;

    private Activity(AccountId source, AccountId target, LocalDateTime timestamp, Money money) {
        this.source = source;
        this.target = target;
        this.money = money;
        this.timestamp = timestamp;
    }

    public static Activity of(AccountId source, AccountId target, LocalDateTime timestamp, Money money) {
        return new Activity(source, target, timestamp, money);
    }

    public static Activity transfer(AccountId source, AccountId target, Money money) {
        return new Activity(source, target, LocalDateTime.now(), money);
    }

    public static Activity withdraw(AccountId source, Money money) {
        return new Activity(source, null, LocalDateTime.now(), money);
    }

    public static Activity deposit(AccountId target, Money money) {
        return new Activity(null, target, LocalDateTime.now(), money);
    }

    @Override
    public int compareTo(Activity another) {
        final long time = this.timestamp.toEpochSecond(ZoneOffset.UTC);
        return Long.valueOf(time - another.timestamp.toEpochSecond(ZoneOffset.UTC)).intValue();
    }
}
