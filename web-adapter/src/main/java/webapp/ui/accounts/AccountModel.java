package webapp.ui.accounts;

import domain.entity.Account;

import java.time.ZoneOffset;
import java.util.Comparator;

public class AccountModel {

    final private Account account;

    private AccountModel(Account account) {
        this.account = account;
    }

    public static AccountModel from(final Account account) {
        return new AccountModel(account);
    }

    String accountId() {
        return this.account.accountId().toString();
    }

    String balance() {
        return String.format("%.2f", this.account.balance().value());
    }

    Account toDomain() {
        return this.account;
    }

    ActivityModel lastActivity() {
        return
            this.account
                .activities()
                .max(Comparator.comparingLong(v -> v.timestamp.toEpochSecond(ZoneOffset.UTC)))
                .map(ActivityModel::from)
                .orElseGet(ActivityModel::empty);
    }
}
