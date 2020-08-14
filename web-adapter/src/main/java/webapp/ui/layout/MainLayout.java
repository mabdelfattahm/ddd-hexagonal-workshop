/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
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
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import webapp.ui.accounts.AccountsGridView;
import webapp.ui.drawer.NavItem;

/**
 * The main layout. Contains the navigation menu.
 *
 * @since 1.0
 */
@Theme(Lumo.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/nav-bar.css", themeFor = "vaadin-app-layout")
@SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
public final class MainLayout extends AppLayout {

    private static final long serialVersionUID = 1620953665106472306L;

    /**
     * Main constructor.
     *
     * @since 1.0
     */
    public MainLayout() {
        this.addToNavbar(MainLayout.getDrawerToggle());
        this.setDrawerOpened(true);
        this.addToNavbar(MainLayout.getTopBar());
        this.addToDrawer(
            new NavItem(VaadinIcon.USERS, AccountsGridView.VIEW_NAME, AccountsGridView.class)
        );
    }

    @Override
    public void onAttach(final AttachEvent event) {
        super.onAttach(event);
        event.getUI().addShortcutListener(MainLayout::logout, Key.KEY_L, KeyModifier.CONTROL);
    }

    /**
     * Initialize drawer toggle.
     *
     * @return Drawer toggle.
     */
    private static DrawerToggle getDrawerToggle() {
        final DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("menu-toggle");
        return toggle;
    }

    /**
     * Initialize top bar.
     *
     * @return Horizontal layout.
     */
    private static HorizontalLayout getTopBar() {
        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        top.setClassName("menu-header");
        final String path =
            VaadinService.getCurrent().resolveResource("images/bank-logo.png");
        final Image image = new Image(path, "");
        final Label title = new Label("Bank");
        title.getStyle().set("font-weight", "600");
        top.add(image, title);
        top.add(title);
        final Button logout =
            MainLayout.createMenuButton("Logout", VaadinIcon.SIGN_OUT.create());
        logout.addClickListener(e -> logout());
        logout.getElement().setAttribute("title", "Logout (Ctrl+L)");
        final HorizontalLayout wrapper = new HorizontalLayout(logout);
        wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        top.addAndExpand(wrapper);
        return top;
    }

    /**
     * Perform logout.
     */
    private static void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().executeJs("window.location.href=''");
    }

    /**
     * Create menu button.
     *
     * @param caption Button caption.
     * @param icon Button icon.
     * @return Menu button.
     */
    private static Button createMenuButton(final String caption, final Icon icon) {
        final Button button = new Button(caption);
        button.setClassName("menu-button");
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);
        button.setIcon(icon);
        icon.setSize("20px");
        return button;
    }
}
