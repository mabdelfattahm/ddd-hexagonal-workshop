package port.out;

import domain.entity.Account;
import domain.value.AccountId;

import java.util.stream.Stream;

public interface LookupAccounts {
    Account byId(AccountId id) throws IllegalArgumentException;
    Stream<Account> all() throws IllegalStateException;
}
