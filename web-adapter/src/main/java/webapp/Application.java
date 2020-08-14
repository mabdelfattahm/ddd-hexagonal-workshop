/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import port.in.CreateAccount;
import port.in.ListAccounts;
import port.in.ListActivities;
import port.in.QueryBalance;
import postgres.PgAdapter;

/**
 * The entry point of the Spring Boot application.
 *
 * @since 1.0
 * @checkstyle HideUtilityClassConstructorCheck (3 lines)
 */
@SuppressWarnings({"PMD.UseUtilityClass", "PMD.ProhibitPublicStaticMethods"})
@SpringBootApplication
public class Application {

    /**
     * Persistence adapter implementation.
     */
    private static final PgAdapter PERSISTENCE = PgAdapter.create();

    /**
     * Main method.
     * @param args Command line arguments.
     */
    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * List accounts port provider.
     *
     * @return List accounts input port.
     * @since 1.0
     */
    public static ListAccounts listAccounts() {
        return new ListAccounts(Application.PERSISTENCE);
    }

    /**
     * Create account port provider.
     *
     * @return Create account input port.
     * @since 1.0
     */
    public static CreateAccount createAccount() {
        return new CreateAccount(Application.PERSISTENCE);
    }

    /**
     * Create query balance provider.
     *
     * @return Balance query input port.
     * @since 1.0
     */
    public static QueryBalance queryBalance() {
        return new QueryBalance(Application.PERSISTENCE);
    }

    /**
     * Create list activities port provider.
     *
     * @return List activities input port.
     * @since 1.0
     */
    public static ListActivities listActivities() {
        return new ListActivities(Application.PERSISTENCE);
    }
}
