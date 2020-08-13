package webapp.ui.accounts;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import port.out.LookupAccounts;
import webapp.ui.layout.MainLayout;

import java.util.function.BiFunction;

@PageTitle("Bank | Accounts")
@Route(value = "accounts", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AccountsGridView extends VerticalLayout {
    public static final String VIEW_NAME = "Accounts";

    private static final long serialVersionUID = 6059422846882659893L;

    private static final BiFunction<AccountModel, ActivityModel, String> transactionSign =
        (account, activity) -> {
            final String sign;
            if (Double.parseDouble(activity.money) == 0) {
                sign = "";
            } else if (account.accountId().equals(activity.source)) {
                sign = "-";
            } else {
                sign = "+";
            }
            return sign;
        };

    private final static TextRenderer<AccountModel> lastTransactionRenderer = new TextRenderer<>((ItemLabelGenerator<AccountModel>) item -> {
        final ActivityModel activity = item.lastActivity();
        final String sign = AccountsGridView.transactionSign.apply(item, activity);
        return String.format("%s%s", sign, activity.money);
    });

    private final static ValueProvider<AccountModel, RouterLink> accountDetailsRouterLink =
        (account) -> new RouterLink(
            AccountDetailsView.VIEW_NAME,
            AccountDetailsView.class,
            account.accountId()
        );

    public AccountsGridView(final LookupAccounts accounts) {
        final Grid<AccountModel> grid = new Grid<>();
        grid.setDataProvider(DataProvider.fromStream(accounts.all().map(AccountModel::from)));
        grid.addColumn(AccountModel::accountId).setHeader("Account Id");
        grid.addColumn(AccountModel::balance).setHeader("Balance");
        grid.addColumn(AccountsGridView.lastTransactionRenderer).setHeader("Last Transaction");
        grid.addComponentColumn(AccountsGridView.accountDetailsRouterLink);
        this.addAndExpand(grid);
    }
}
