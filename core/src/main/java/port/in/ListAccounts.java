package port.in;

import domain.entity.Account;
import port.out.LookupAccounts;

import java.util.stream.Stream;

public class ListAccounts {

    final LookupAccounts accounts;

    public ListAccounts(LookupAccounts accounts) {
        this.accounts = accounts;
    }

    public Stream<Account> accounts() {
        return this.accounts.all();
    }

}
