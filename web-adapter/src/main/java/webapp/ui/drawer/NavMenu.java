/*
 * Developed 2020 by Ergonomics AG, Zurich, CH
 * All Rights Reserved. Confidential.
 */
package webapp.ui.drawer;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.UnorderedList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Navigation Menu.
 *
 * @since 1.0
 */
@CssImport("./styles/nav-menu.css")
@SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
public final class NavMenu extends Nav {

    private static final long serialVersionUID = 5859087231507095472L;

    /**
     * Class name.
     */
    private static final String CLASS_NAME = "nav-menu";

    /**
     * Menu Items.
     */
    private final UnorderedList list;

    /**
     * Constructor.
     *
     * @since 1.0
     */
    public NavMenu() {
        this.setClassName(NavMenu.CLASS_NAME);
        this.list = new UnorderedList();
        this.add(this.list);
    }

    /**
     * Add navigation item.
     *
     * @param item Navigation item.
     * @since 1.0
     */
    public void addNavItem(final NavItem item) {
        this.list.add(item);
    }

    /**
     * Navigation items.
     *
     * @return Navigation items.
     * @since 1.0
     */
    public List<NavItem> navigationItems() {
        return this.list.getChildren().map(i -> (NavItem) i).collect(Collectors.toList());
    }
}
