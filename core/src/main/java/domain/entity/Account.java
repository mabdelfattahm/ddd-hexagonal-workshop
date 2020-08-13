package domain.entity;

import common.PreConditions;
import domain.value.AccountId;
import domain.value.Activity;
import domain.value.ActivityWindow;
import domain.value.Money;
import exception.InsufficientFundsException;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class Account {

    private final AccountId id;
    private final Money baselineBalance;
    private final ActivityWindow window;

    private Account(AccountId id, Money baselineBalance, ActivityWindow window) {
        this.id = id;
        this.baselineBalance = baselineBalance;
        this.window = window;
    }

    public static Account of(AccountId id, Money baselineBalance) {
        return new Account(id, baselineBalance, ActivityWindow.create());
    }

    public static Account of(AccountId id, Money baselineBalance, ActivityWindow window) {
        return new Account(id, baselineBalance, window);
    }

    public AccountId accountId() {
        return this.id;
    }

    public Money balance() {
        return
            baselineBalance
                .plus(window.depositedIntoAccount(id))
                .minus(window.withdrawnFromAccount(id));
    }

    public Stream<Activity> activities(LocalDateTime since) {
        return this.window.filtered(since);
    }

    public Stream<Activity> activities() {
        return this.window.all();
    }

    public Stream<Activity> unsavedActivities() {
        return this.window.newlyAdded();
    }

    public void withdraw(Money money) throws InsufficientFundsException {
        canWithdraw(money);
        window.addActivity(Activity.withdraw(this.id, money));
    }

    public void deposit(Money money) {
        window.addActivity(Activity.deposit(this.id, money));
    }

    public void transfer(AccountId target, Money money) throws InsufficientFundsException {
        canWithdraw(money);
        window.addActivity(Activity.transfer(this.id, target, money));
    }

    private void canWithdraw(Money money) throws InsufficientFundsException {
        PreConditions.require(this.balance().minus(money), Money::isPositiveOrZero, InsufficientFundsException::new);
    }



}
