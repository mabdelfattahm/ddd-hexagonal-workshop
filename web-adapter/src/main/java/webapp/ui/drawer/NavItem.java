/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp.ui.drawer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import java.util.Optional;

/**
 * Navigation item view.
 *
 * @since 1.0
 */
@CssImport("./styles/nav-item.css")
@SuppressWarnings({
    "PMD.ConstructorOnlyInitializesOrCallOtherConstructors",
    "PMD.OnlyOneConstructorShouldDoInitialization"
})
public final class NavItem extends ListItem {

    private static final long serialVersionUID = -6403372894979165972L;

    /**
     * Class name.
     */
    private static final String CLASS_NAME = "nav-item";

    /**
     * Component.
     */
    private final Component component;

    /**
     * Navigation target.
     */
    private final Class<? extends Component> target;

    /**
     * Item text.
     */
    private final String text;

    /**
     * Helper constructor.
     *
     * @param icon Navigation item icon.
     * @param text Navigation item text.
     * @param target Navigation item target view class.
     */
    public NavItem(
        final VaadinIcon icon,
        final String text,
        final Class<? extends Component> target
    ) {
        this(text, target);
        this.component.getElement().insertChild(0, new Icon(icon).getElement());
    }

    /**
     * Helper constructor.
     *
     * @param text Navigation item text.
     * @param target Navigation item target view class.
     */
    public NavItem(final String text, final Class<? extends Component> target) {
        this.setClassName(NavItem.CLASS_NAME);
        this.text = text;
        this.target = target;
        this.component = NavItem.initComponent(text, target);
        this.add(this.component);
    }

    /**
     * Target accessor.
     *
     * @return Navigation item target view class.
     * @since 1.0
     */
    public Class<? extends Component> getTarget() {
        return this.target;
    }

    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Initialize router link of a navigation item.
     *
     * @param text Navigation item text.
     * @param target Navigation item target view class.
     * @return Router link as a component.
     */
    private static Component initComponent(
        final String text,
        final Class<? extends Component> target
    ) {
        final HasStyle component =
            Optional
                .ofNullable(target)
                .map(
                    t -> {
                        final RouterLink link = new RouterLink(null, t);
                        link.add(new Span(text));
                        link.setHighlightCondition(HighlightConditions.sameLocation());
                        return (HasStyle) link;
                    }
                )
                .orElseGet(() -> new Div(new Span(text)));
        component.setClassName(String.format("%s__link", NavItem.CLASS_NAME));
        return (Component) component;
    }
}
