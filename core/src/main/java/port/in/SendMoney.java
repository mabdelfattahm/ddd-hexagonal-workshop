/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package port.in;

import domain.value.AccountId;
import domain.value.Money;
import exception.ConcurrentOperationException;
import exception.InsufficientFundsException;
import port.out.LookupAccounts;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Send money input port.
 *
 * @since 1.0
 */
public class SendMoney {

    /**
     * Lookup accounts port.
     */
    private final LookupAccounts lookup;

    /**
     * Locked accounts map.
     */
    private static final Set<AccountId> LOCKED_ACCOUNTS = ConcurrentHashMap.newKeySet();

    /**
     * Main constructor.
     *
     * @param lookup Lookup accounts port.
     * @since 1.0
     */
    public SendMoney(final LookupAccounts lookup) {
        this.lookup = lookup;
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
        SendMoney.lock(source, target);
        this.lookup.byId(source).transfer(target, money);
        SendMoney.release(source, target);
    }

    /**
     * Lock Accounts to prevent concurrent modifications.
     *
     * @param accounts Accounts to lock.
     */
    private static void lock(final AccountId... accounts) {
        SendMoney.LOCKED_ACCOUNTS.addAll(Arrays.asList(accounts));
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
                .map(SendMoney.LOCKED_ACCOUNTS::contains)
                .reduce(false, Boolean::logicalOr);
    }

}
