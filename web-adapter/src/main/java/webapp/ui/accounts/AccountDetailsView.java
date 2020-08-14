/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp.ui.accounts;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import domain.value.AccountId;
import webapp.Application;
import webapp.ui.layout.MainLayout;

/**
 * Account details view.
 *
 * @since 1.0
 */
@PageTitle("Bank | Account Details")
@Route(value = "account", layout = MainLayout.class)
@SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
public final class AccountDetailsView extends VerticalLayout implements HasUrlParameter<String> {

    /**
     * View name.
     */
    public static final String VIEW_NAME = "Account Details";

    private static final long serialVersionUID = -2586991101140415320L;

    /**
     * Id text.
     */
    private final H3 id;

    /**
     * Balance text.
     */
    private final Text balance;

    /**
     * Activity grid view.
     */
    private final Grid<ActivityModel> activities;

    /**
     * Main constructor.
     */
    private AccountDetailsView() {
        this.id = new H3();
        this.balance = new Text("");
        this.activities = new Grid<>();
        this.activities.addColumn(model -> model.source).setHeader("Source Account");
        this.activities.addColumn(model -> model.target).setHeader("Target Account");
        this.activities.addColumn(model -> model.money).setHeader("Amount");
        this.activities.addColumn(model -> model.timestamp).setHeader("Time");
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.CENTER);
        layout.addAndExpand(this.id);
        final VerticalLayout another = new VerticalLayout();
        another.setAlignItems(Alignment.CENTER);
        another.add(new H4("Balance"), this.balance);
        another.setWidth("fit-content");
        layout.add(another);
        this.add(layout, this.activities);
    }

    @Override
    public void setParameter(final BeforeEvent event, final String parameter) {
        final AccountId account = AccountId.with(parameter);
        this.id.setText(parameter);
        this.balance.setText(
            String.format(
                "%.2f",
                Application.queryBalance().getAccountBalance(account).value()
            )
        );
        this.activities.setDataProvider(
            DataProvider.fromStream(
                Application.listActivities().byAccountId(account).map(ActivityModel::from)
            )
        );
    }
}
