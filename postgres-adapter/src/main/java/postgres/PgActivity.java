package postgres;

import domain.value.AccountId;
import domain.value.Activity;
import domain.value.Money;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

public class PgActivity {
    final String source;

    final String target;

    final Timestamp timestamp;

    final Double money;

    public PgActivity(String source, String target, Timestamp timestamp, Double money) {
        this.source = source;
        this.target = target;
        this.timestamp = timestamp;
        this.money = money;
    }

    Activity toDomain() {
        return
            Activity.of(
                Optional.ofNullable(this.source).map(AccountId::of).orElse(null),
                Optional.ofNullable(this.target).map(AccountId::of).orElse(null),
                this.timestamp.toLocalDateTime(),
                Money.of(this.money)
            );
    }

    boolean selfValidate() {
        return
            Objects.nonNull(this.timestamp)
                && Objects.nonNull(this.money)
                && (Objects.nonNull(this.source) || Objects.nonNull(this.target));
    }
}
