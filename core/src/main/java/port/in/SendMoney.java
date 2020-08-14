/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.in;

import domain.entity.Account;
import domain.value.AccountId;
import domain.value.Money;
import exception.ConcurrentOperationException;
import exception.InsufficientFundsException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import port.out.LookupAccounts;
import port.out.StoreActivity;

/**
 * Send money input port.
 *
 * @since 1.0
 */
public class SendMoney {

    /**
     * Locked accounts map.
     */
    private static final Set<AccountId> LOCKED_ACCOUNTS = ConcurrentHashMap.newKeySet();

    /**
     * Lookup accounts port.
     */
    private final LookupAccounts lookup;

    /**
     * Lookup accounts port.
     */
    private final StoreActivity activities;

    /**
     * Main constructor.
     *
     * @param lookup Lookup accounts port.
     * @param activities Update account activities.
     * @since 1.0
     */
    public SendMoney(final LookupAccounts lookup, final StoreActivity activities) {
        this.lookup = lookup;
        this.activities = activities;
    }

    /**
     * Withdraw money from account.
     *
     * @param id Account Id.
     * @param money Money account.
     * @throws ConcurrentOperationException If any of accounts is locked in another transaction.
     * @throws InsufficientFundsException If the source account does not have enough money.
     * @since 1.0
     */
    public void withdraw(
        final AccountId id,
        final Money money
    ) throws ConcurrentOperationException, InsufficientFundsException {
        if (SendMoney.anyLocked(id)) {
            throw new ConcurrentOperationException();
        }
        final Account account = this.lookup.byId(id);
        SendMoney.lock(id);
        this.activities.storeActivity(account.withdraw(money));
        SendMoney.release(id);
    }

    /**
     * Deposit money into account.
     *
     * @param id Account Id.
     * @param money Money account.
     * @throws ConcurrentOperationException If any of accounts is locked in another transaction.
     * @since 1.0
     */
    public void deposit(
        final AccountId id,
        final Money money
    ) throws ConcurrentOperationException {
        if (SendMoney.anyLocked(id)) {
            throw new ConcurrentOperationException();
        }
        final Account account = this.lookup.byId(id);
        SendMoney.lock(id);
        this.activities.storeActivity(account.deposit(money));
        SendMoney.release(id);
    }

    /**
     * Send money between accounts.
     *
     * @param source Source account.
     * @param target Target account.
     * @param money Money account.
     * @throws ConcurrentOperationException If any of accounts is locked in another transaction.
     * @throws InsufficientFundsException If the source account does not have enough money.
     * @since 1.0
     */
    public void sendMoney(
        final AccountId source,
        final AccountId target,
        final Money money
    ) throws ConcurrentOperationException, InsufficientFundsException {
        if (SendMoney.anyLocked(source, target)) {
            throw new ConcurrentOperationException();
        }
        final Account sor = this.lookup.byId(source);
        final Account trg = this.lookup.byId(target);
        SendMoney.lock(source, target);
        this.activities.storeActivity(sor.transfer(trg.accountId(), money));
        SendMoney.release(source, target);
    }

    /**
     * Lock Accounts to prevent concurrent modifications.
     *
     * @param accounts Accounts to lock.
     */
    private static void lock(final AccountId... accounts) {
        SendMoney
            .LOCKED_ACCOUNTS
            .addAll(Arrays.stream(accounts).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    /**
     * Release lock on accounts.
     *
     * @param accounts Accounts to release.
     */
    private static void release(final AccountId... accounts) {
        SendMoney.LOCKED_ACCOUNTS.removeAll(Arrays.asList(accounts));
    }

    /**
     * Check if any of the accounts is locked.
     *
     * @param accounts Accounts to check.
     * @return Is any of these accounts locked.
     */
    private static boolean anyLocked(final AccountId... accounts) {
        return
            Arrays
                .stream(accounts)
                .filter(Objects::nonNull)
                .map(SendMoney.LOCKED_ACCOUNTS::contains)
                .reduce(false, Boolean::logicalOr);
    }

}
