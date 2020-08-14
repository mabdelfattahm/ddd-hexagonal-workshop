/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
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
import webapp.Application;
import webapp.ui.layout.MainLayout;

/**
 * Accounts grid view.
 *
 * @since 1.0
 */
@PageTitle("Bank | Accounts")
@Route(value = "accounts", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
public final class AccountsGridView extends VerticalLayout {

    /**
     * View name.
     */
    public static final String VIEW_NAME = "Accounts";

    private static final long serialVersionUID = 6059422846882659893L;

    /**
     * Activity to text renderer.
     */
    @SuppressWarnings("PMD.LongVariable")
    private static final TextRenderer<AccountModel> TRANSACTION_RENDERER =
        new TextRenderer<>(new TransactionLabel());

    /**
     * Main constructor.
     *
     * @since 1.0
     */
    public AccountsGridView() {
        final Grid<AccountModel> grid = new Grid<>();
        grid.setDataProvider(
            DataProvider.fromStream(
                Application
                    .listAccounts()
                    .accounts()
                    .map(AccountModel::from)
            )
        );
        grid.addColumn(AccountModel::accountId).setHeader("Account Id");
        grid.addColumn(AccountModel::balance).setHeader("Balance");
        grid.addColumn(AccountsGridView.TRANSACTION_RENDERER).setHeader("Last Transaction");
        grid.addComponentColumn(new DetailsRouterLink());
        this.addAndExpand(grid);
    }

    /**
     * Get transaction sign.
     *
     * @param account Account model.
     * @param activity Activity Model.
     * @return String representation of the sign.
     * @since 1.0
     */
    private static String sign(final AccountModel account, final ActivityModel activity) {
        final String sign;
        if (Double.parseDouble(activity.money) == 0) {
            sign = "";
        } else if (account.accountId().equals(activity.source)) {
            sign = "-";
        } else {
            sign = "+";
        }
        return sign;
    }

    /**
     * Last transaction label generator.
     *
     * @since 1.0
     */
    private static class TransactionLabel implements ItemLabelGenerator<AccountModel> {

        private static final long serialVersionUID = -5904090433916952102L;

        @Override
        public String apply(final AccountModel account) {
            final ActivityModel activity = account.lastActivity();
            final String sign = AccountsGridView.sign(account, activity);
            return String.format("%s%s", sign, activity.money);
        }
    }

    /**
     * Create account details router links from account models.
     *
     * @since 1.0
     */
    private static class DetailsRouterLink implements ValueProvider<AccountModel, RouterLink> {

        private static final long serialVersionUID = 3056537948325562753L;

        @Override
        public RouterLink apply(final AccountModel account) {
            return
                new RouterLink(
                    AccountDetailsView.VIEW_NAME,
                    AccountDetailsView.class,
                    account.accountId()
                );
        }
    }
}
