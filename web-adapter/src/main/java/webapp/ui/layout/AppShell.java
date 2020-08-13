package webapp.ui.layout;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;

/**
 * App shell.
 *
 * @since 1.0
 */
@PWA(name = "Bank", shortName = "Bank", enableInstallPrompt = false)
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public final class AppShell implements AppShellConfigurator {

    private static final long serialVersionUID = 4744280944777588691L;

    @Override
    public void configurePage(final AppShellSettings settings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes");
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
        settings.addFavIcon("icon", "frontend/images/favicons/favicon.ico", "256x256");
    }
}
