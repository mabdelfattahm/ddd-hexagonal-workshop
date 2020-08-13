package port.in;

import domain.value.AccountId;
import domain.value.Money;
import port.out.LookupAccounts;

public class QueryBalance {

    private final LookupAccounts accounts;

    public QueryBalance(LookupAccounts accounts) {
        this.accounts = accounts;
    }

    Money getAccountBalance(AccountId id) {
        return this.accounts.byId(id).balance();
    }
}
