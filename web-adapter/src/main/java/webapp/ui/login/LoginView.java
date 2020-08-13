package webapp.ui.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;
import webapp.ui.accounts.AccountDetailsView;
import webapp.ui.accounts.AccountsGridView;

/**
 * Login view.
 *
 * @since 1.0
 */
@Route("login")
@PageTitle("Login | Bank")
public final class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private static final long serialVersionUID = 2813883384190122036L;

    /**
     * Login form.
     */
    private final LoginForm login;

    /**
     * Constructor.
     *
     * @since 1.0
     */
    public LoginView() {
        this.login = LoginView.initForm(this);
    }

    @Override
    public void beforeEnter(final BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            this.login.setError(true);
        }
    }

    /**
     * Initialize view and login form.
     *
     * @param layout Vertical layout.
     * @return Login form.
     */
    private static LoginForm initForm(final VerticalLayout layout) {
        final LoginForm form = new LoginForm();
        form.setAction("login");
        layout.addClassName("login-view");
        layout.setSizeFull();
        layout.setAlignItems(Alignment.CENTER);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.add(new H1("Bank"), form);
        return form;
    }
}
