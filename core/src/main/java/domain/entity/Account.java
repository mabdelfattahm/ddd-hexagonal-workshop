/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package domain.entity;

import common.PreConditions;
import domain.value.AccountId;
import domain.value.Activity;
import domain.value.ActivityWindow;
import domain.value.Money;
import exception.InsufficientFundsException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * Account domain model.
 *
 * @since 1.0
 */
@SuppressWarnings({"PMD.ProhibitPublicStaticMethods", "PMD.TooManyMethods"})
public final class Account {

    /**
     * Account Id.
     */
    private final AccountId id;

    /**
     * Baseline balance.
     */
    private final Money starting;

    /**
     * Activity window.
     */
    private final ActivityWindow window;

    /**
     * Main constructor.
     *
     * @param id Account Id.
     * @param starting Starting balance.
     * @param window Activity window.
     */
    private Account(final AccountId id, final Money starting, final ActivityWindow window) {
        this.id = id;
        this.starting = starting;
        this.window = window;
    }

    /**
     * Factory method. This will create an empty activity window.
     *
     * @param id Account Id.
     * @param starting Starting balance.
     * @return Account instance.
     * @since 1.0
     */
    public static Account with(final AccountId id, final Money starting) {
        return new Account(id, starting, ActivityWindow.create());
    }

    /**
     * Factory method. This will use the passed activity window to calculate balance.
     *
     * @param id Account Id.
     * @param starting Starting balance.
     * @param window Activity window
     * @return Account instance.
     * @since 1.0
     */
    public static Account with(
        final AccountId id,
        final Money starting,
        final ActivityWindow window
    ) {
        return new Account(id, starting, window);
    }

    /**
     * Account Id accessor.
     *
     * @return Account Id.
     * @since 1.0
     */
    public AccountId accountId() {
        return this.id;
    }

    /**
     * Calculate account balance taking the account activity into consideration.
     *
     * @return Account balance.
     * @since 1.0
     */
    public Money balance() {
        return
            this.starting
                .plus(this.window.depositedIntoAccount(this.id))
                .minus(this.window.withdrawnFromAccount(this.id));
    }

    /**
     * Activities from the activity window that happened after the provided datetime.
     *
     * @param datetime Datetime to start the stream from.
     * @return Stream of activities.
     * @since 1.0
     */
    public Stream<Activity> activities(final LocalDateTime datetime) {
        return this.window.filtered(datetime);
    }

    /**
     * All activities from the activity window.
     *
     * @return Stream of activities.
     * @since 1.0
     */
    public Stream<Activity> activities() {
        return this.window.all();
    }

    /**
     * Only activities that were done after account was loaded.
     *
     * @return Stream of activities.
     * @since 1.0
     */
    public Stream<Activity> unsavedActivities() {
        return this.window.newlyAdded();
    }

    /**
     * Withdraw a certain amount of money.
     *
     * @param money Amount to withdraw.
     * @throws InsufficientFundsException If the account balance is not enough.
     * @since 1.0
     */
    public void withdraw(final Money money) throws InsufficientFundsException {
        this.canWithdraw(money);
        this.window.addActivity(Activity.withdraw(this.id, money));
    }

    /**
     * Deposit money into account.
     *
     * @param money Amount to deposit.
     * @since 1.0
     */
    public void deposit(final Money money) {
        this.window.addActivity(Activity.deposit(this.id, money));
    }

    /**
     * Transfer money to another account.
     *
     * @param target Target account Id.
     * @param money Amount to transfer.
     * @throws InsufficientFundsException If this account does not have enough money to transfer.
     * @since 1.0
     */
    public void transfer(
        final AccountId target,
        final Money money
    ) throws InsufficientFundsException {
        this.canWithdraw(money);
        this.window.addActivity(Activity.transfer(this.id, target, money));
    }

    /**
     * Is the account balance sufficient.
     *
     * @param money Amount to check if the account has or not.
     * @throws InsufficientFundsException If the account balance is less than the amount of money.
     */
    private void canWithdraw(final Money money) throws InsufficientFundsException {
        PreConditions.require(
            this.balance().minus(money),
            Money::isPositiveOrZero,
            InsufficientFundsException::new
        );
    }
}
