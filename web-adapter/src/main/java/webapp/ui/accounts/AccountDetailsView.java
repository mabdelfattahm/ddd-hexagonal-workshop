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
import domain.entity.Account;
import domain.value.AccountId;
import port.out.LookupAccounts;
import webapp.ui.layout.MainLayout;

@PageTitle("Bank | Account Details")
@Route(value = "account", layout = MainLayout.class)
public class AccountDetailsView extends VerticalLayout implements HasUrlParameter<String>{

    public static final String VIEW_NAME = "Account Details";

    private final LookupAccounts accounts;

    private final H3 id;
    private final Text balance;
    private final Grid<ActivityModel> activities;

    private AccountDetailsView(LookupAccounts accounts) {
        this.accounts = accounts;
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
    public void setParameter(BeforeEvent beforeEvent, String s) {
        final Account account = this.accounts.byId(AccountId.with(s));
        this.id.setText(account.accountId().toString());
        this.balance.setText(String.format("%.2f", account.balance().value()));
        this.activities.setDataProvider(
            DataProvider.fromStream(
                account.activities().map(ActivityModel::from)
            )
        );
    }
}
