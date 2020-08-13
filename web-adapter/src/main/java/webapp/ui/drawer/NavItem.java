package webapp.ui.drawer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import java.util.Optional;


@CssImport("./styles/nav-item.css")
@SuppressWarnings({
    "PMD.ConstructorOnlyInitializesOrCallOtherConstructors", "PMD.AvoidDuplicateLiterals"
})
public class NavItem extends ListItem {

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

    public NavItem(final VaadinIcon icon, final String text, final Class<? extends Component> target) {
        this(text, target);
        this.component.getElement().insertChild(0, new Icon(icon).getElement());
    }

    public NavItem(final String text, final Class<? extends Component> target) {
        this.setClassName(NavItem.CLASS_NAME);
        this.text = text;
        this.target = target;
        this.component = NavItem.initComponent(text, target);
        this.add(this.component);
    }

    public boolean isHighlighted(final AfterNavigationEvent event) {
        return
            Optional
                .of(this.component)
                .filter(c -> c instanceof RouterLink)
                .map(c -> (RouterLink) c)
                .map(c -> c.getHighlightCondition().shouldHighlight(c, event))
                .orElse(false);
    }

    public Class<? extends Component> getTarget() {
        return this.target;
    }

    @Override
    public final String getText() {
        return this.text;
    }

    private static Component initComponent(final String text, final Class<? extends Component> target) {
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
