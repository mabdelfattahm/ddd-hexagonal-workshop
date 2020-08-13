package port.in;

import domain.value.AccountId;
import domain.value.Activity;
import port.out.LookupAccounts;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class ListActivities {

    private final LookupAccounts accounts;

    public ListActivities(LookupAccounts accounts) {
        this.accounts = accounts;
    }

    Stream<Activity> byAccountId(AccountId id, LocalDateTime since) {
        return accounts.byId(id).activities(since);
    }
}
