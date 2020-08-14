/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp.config;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import webapp.ui.login.LoginView;

/**
 * Configure Vaadin service initialization.
 *
 * @since 1.0
 */
@Component
@Profile("prod")
public final class WebappServiceInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(final ServiceInitEvent event) {
        event.getSource()
            .addUIInitListener(
                uiEvent -> {
                    final UI gui = uiEvent.getUI();
                    gui.addBeforeEnterListener(
                        WebappServiceInitListener::authenticateNavigation
                    );
                }
            );
    }

    /**
     * Authenticate Vaadin flow navigation.
     *
     * @param event Before entering UI screen event.
     */
    private static void authenticateNavigation(final BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())
            && !WebappServiceInitListener.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }

    /**
     * Is the user logged in.
     *
     * @return Boolean.
     */
    private static boolean isUserLoggedIn() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
            && !(auth instanceof AnonymousAuthenticationToken)
            && auth.isAuthenticated();
    }
}
