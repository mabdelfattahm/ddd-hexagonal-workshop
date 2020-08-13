package port.in;

import domain.entity.Account;
import domain.value.AccountId;
import domain.value.Money;

public class CreateAccount {
    public Account create(Money starting) {
        return Account.of(AccountId.create(), starting);
    }
}
