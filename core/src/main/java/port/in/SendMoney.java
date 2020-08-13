package port.in;

import domain.value.AccountId;
import domain.value.Money;
import exception.ConcurrentOperationException;
import exception.InsufficientFundsException;
import port.out.LookupAccounts;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SendMoney {

    private final LookupAccounts accounts;

    private static final Set<AccountId> LOCKED_ACCOUNTS = ConcurrentHashMap.newKeySet();

    public SendMoney(LookupAccounts accounts) {
        this.accounts = accounts;
    }

    void sendMoney(AccountId source, AccountId target, Money money) throws ConcurrentOperationException, InsufficientFundsException {
        if (SendMoney.anyLocked(source, target)) {
            throw new ConcurrentOperationException();
        }
        SendMoney.lock(source, target);
        this.accounts.byId(source).transfer(target, money);
        SendMoney.release(source, target);
    }

    private static void lock(AccountId... ids) {
        SendMoney.LOCKED_ACCOUNTS.addAll(Arrays.asList(ids));
    }


    private static void release(AccountId... ids) {
        SendMoney.LOCKED_ACCOUNTS.removeAll(Arrays.asList(ids));
    }

    private static boolean anyLocked(AccountId... ids) {
        return
            Arrays
                .stream(ids)
                .map(SendMoney.LOCKED_ACCOUNTS::contains)
                .reduce(false, Boolean::logicalOr);
    }

}
