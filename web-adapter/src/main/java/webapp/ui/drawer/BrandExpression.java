/*
 * Developed 2020 by Ergonomics AG, Zurich, CH
 * All Rights Reserved. Confidential.
 */
package webapp.ui.drawer;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;

/**
 * Brand Component.
 *
 * @since 1.0
 */
@CssImport("./styles/brand-expression.css")
@SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
public final class BrandExpression extends Div {

    private static final long serialVersionUID = -7315564043249565908L;

    /**
     * Class name.
     */
    private static final String CLASS_NAME = "brand-expression";

    /**
     * Constructor.
     *
     * @param text Label text.
     * @since 1.0
     */
    public BrandExpression(final String text) {
        this.setClassName(BrandExpression.CLASS_NAME);
        final H3 title = new H3(text);
        title.addClassName(String.format("%s__title", BrandExpression.CLASS_NAME));
        this.add(title);
    }
}
