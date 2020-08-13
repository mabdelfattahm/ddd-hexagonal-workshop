package webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import postgres.PgAdapter;

/**
 * The entry point of the Spring Boot application.
 *
 * @since 1.0
 * @checkstyle HideUtilityClassConstructorCheck (3 lines)
 */
@SuppressWarnings("PMD.UseUtilityClass")
@SpringBootApplication
public class Application {
    /**
     * Main method.
     * @param args Command line arguments.
     */
    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    PgAdapter persistenceAdapter() {
        return PgAdapter.create();
    }

}
