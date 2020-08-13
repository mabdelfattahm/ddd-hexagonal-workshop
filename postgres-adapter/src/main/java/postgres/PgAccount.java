package postgres;

import domain.entity.Account;
import domain.value.AccountId;
import domain.value.Activity;
import domain.value.ActivityWindow;
import domain.value.Money;

import java.util.List;
import java.util.stream.Collectors;


public class PgAccount {

    final String id;

    final Double startBalance;

    final List<PgActivity> activities;

    public PgAccount(String id, Double startBalance, List<PgActivity> activities) {
        this.id = id;
        this.startBalance = startBalance;
        this.activities = activities;
    }

    public Account toDomain() {
        final List<Activity> activities =
            this.activities
                .stream()
                .filter(PgActivity::selfValidate)
                .map(PgActivity::toDomain)
                .collect(Collectors.toList());
        final AccountId id = AccountId.of(this.id);
        final ActivityWindow window = ActivityWindow.unmodifiable(activities);
        final Money deposited = window.depositedIntoAccount(id);
        final Money withdrawn = window.withdrawnFromAccount(id);
        final Money balance = Money.of(this.startBalance).plus(deposited).minus(withdrawn);
        return Account.of(id, balance);
    }
}
