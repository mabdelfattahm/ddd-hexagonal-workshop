package webapp.services;

import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.stereotype.Service;
import port.out.LookupAccounts;
import webapp.ui.accounts.AccountModel;

@Service
public class AccountsService {

    private final LookupAccounts accounts;

    public AccountsService(final LookupAccounts accounts) {
        this.accounts = accounts;
    }

    DataProvider<AccountModel, Void> accounts() {
        return
            DataProvider.
                fromCallbacks(
                    q -> accounts.all().skip(q.getOffset()).limit(q.getLimit()).map(AccountModel::from),
                    q -> (int) accounts.all().skip(q.getOffset()).limit(q.getLimit()).count()
                );
    }
}
