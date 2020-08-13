package webapp.ui.layout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import webapp.ui.accounts.AccountsGridView;
import webapp.ui.drawer.NavItem;
import java.util.stream.Stream;

/**
 * The main layout. Contains the navigation menu.
 */
@Theme(value = Lumo.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/nav-bar.css", themeFor = "vaadin-app-layout")
public class MainLayout extends AppLayout implements RouterLayout {

    public MainLayout() {

        // Header of the menu (the navbar)

        final DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassName("menu-toggle");
        addToNavbar(drawerToggle);
        setDrawerOpened(true);

        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        top.setClassName("menu-header");

        final String resolvedImage =
            VaadinService.getCurrent().resolveResource("images/bank-logo.png");

        final Image image = new Image(resolvedImage, "");
        final Label title = new Label("Bank");
        title.getStyle().set("font-weight", "600");
        top.add(image, title);
        top.add(title);

        final Button logout  = createMenuButton("Logout", VaadinIcon.SIGN_OUT.create());
        logout.addClickListener(e -> logout());
        logout.getElement().setAttribute("title", "Logout (Ctrl+L)");
        final HorizontalLayout wrapper = new HorizontalLayout(logout);
        wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        top.addAndExpand(wrapper);
        addToNavbar(top);

        this.addToDrawer(
            new NavItem(VaadinIcon.USERS, AccountsGridView.VIEW_NAME, AccountsGridView.class)
        );
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attachEvent.getUI().addShortcutListener(this::logout, Key.KEY_L, KeyModifier.CONTROL);
    }

    private void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().executeJs("window.location.href=''");
    }

    private Button createMenuButton(String caption, Icon icon) {
        final Button routerButton = new Button(caption);
        routerButton.setClassName("menu-button");
        routerButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        routerButton.setIcon(icon);
        icon.setSize("20px");
        return routerButton;
    }

    /**
     * Active navigation item.
     *
     * @param event Navigation event.
     * @return Navigation item.
     */
    private NavItem getActiveItem(final AfterNavigationEvent event) {
        return
            this.getChildren()
                .filter(NavItem.class::isInstance)
                .map(NavItem.class::cast)
                .filter(item -> item.isHighlighted(event))
                .findFirst()
                .orElse(null);
    }

    private Stream<NavItem> navItems() {
        return
            this.getChildren()
                .filter(NavItem.class::isInstance)
                .map(NavItem.class::cast);
    }

}
