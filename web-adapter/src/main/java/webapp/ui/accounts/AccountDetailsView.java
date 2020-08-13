package webapp.ui.accounts;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import domain.entity.Account;
import domain.value.AccountId;
import port.out.LookupAccounts;
import webapp.ui.layout.MainLayout;

@Route(value = "account", layout = MainLayout.class)
public class AccountDetailsView extends VerticalLayout implements HasUrlParameter<String>{

    public static final String VIEW_NAME = "Account Details";

    private final LookupAccounts accounts;

    private final H2 id;
    private final Text balance;
    private final Grid<ActivityModel> activities;

    private AccountDetailsView(LookupAccounts accounts) {
        this.accounts = accounts;
        this.id = new H2();
        this.balance = new Text("");
        this.activities = new Grid<>();
        this.activities.addColumn(model -> model.source);
        this.activities.addColumn(model -> model.target);
        this.activities.addColumn(model -> model.source);
        this.activities.addColumn(model -> model.source);
        final HorizontalLayout layout = new HorizontalLayout();
        layout.addAndExpand(this.id);
        layout.add(this.balance);
        this.add(layout, this.activities);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        final Account account = this.accounts.byId(AccountId.of(s));
        this.id.setText(account.accountId().toString());
        this.balance.setText(String.format("%.2f", account.balance().value()));
        this.activities.setDataProvider(
            DataProvider.fromStream(
                account.activities().map(ActivityModel::from)
            )
        );
    }
}
