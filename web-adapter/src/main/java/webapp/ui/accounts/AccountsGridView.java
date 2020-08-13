package webapp.ui.accounts;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import port.out.LookupAccounts;
import webapp.ui.layout.MainLayout;

@Route(value = "accounts", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AccountsGridView extends VerticalLayout {
    public static final String VIEW_NAME = "Accounts";
    private static final long serialVersionUID = 6059422846882659893L;

    private final Grid<AccountModel> grid;

    public AccountsGridView(final LookupAccounts accounts) {
        this.grid = new Grid<>();
        this.grid.setDataProvider(DataProvider.fromStream(accounts.all().map(AccountModel::from)));
        this.grid.addColumn(AccountModel::accountId);
        this.grid.addColumn(AccountModel::balance);
        this.addAndExpand(this.grid);
    }
}
