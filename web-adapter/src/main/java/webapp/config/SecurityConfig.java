package webapp.config;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Spring security config.
 *
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Restful API config using custom token.
     *
     * @since 1.0
     */
    @Order(1)
    @Configuration
    public static class RestConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected final void configure(final HttpSecurity http) throws Exception {
            http.antMatcher("/api/v1/**")
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .addFilter(new TokenAuthenticationFilter(this.authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    /**
     * Token filter.
     *
     * @since 1.0
     */
    public static final class TokenAuthenticationFilter extends BasicAuthenticationFilter {

        /**
         * Header name for token string.
         */
        private static final String HEADER = "X-Authorization";

        /**
         * Constructor.
         *
         * @param authenticator Authentication manager.
         * @since 1.0
         */
        public TokenAuthenticationFilter(final AuthenticationManager authenticator) {
            super(authenticator);
        }

        @Override
        public void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain
        ) throws ServletException, IOException {
            final String token =
                Optional
                    .ofNullable(request.getHeader(TokenAuthenticationFilter.HEADER))
                    .orElse("");
            if (!token.isEmpty()) {
                chain.doFilter(request, response);
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * Vaadin web app security config.
     *
     * @since 1.0
     */
    @Order(2)
    @Configuration
    public static class WebConfiguration extends WebSecurityConfigurerAdapter {

        /**
         * Login URL.
         */
        private static final String LOGIN_URL = "/login";

        /**
         * Login error URL.
         */
        private static final String LOGIN_FAILURE_URL = "/login?error";

        /**
         * LDAP server url.
         */
        @SuppressWarnings("NullAway.Init")
        @Value("${ldap.server.url}")
        private String server;

        /**
         * LDAP domain pattern.
         */
        @SuppressWarnings("NullAway.Init")
        @Value("${ldap.server.dnPatterns}")
        private String pattern;

        /**
         * LDAP group search base.
         */
        @SuppressWarnings("NullAway.Init")
        @Value("${ldap.server.groupSearchBase}")
        private String group;

        /**
         * LDAP password attribute.
         */
        @SuppressWarnings("NullAway.Init")
        @Value("${ldap.server.passwordAttribute}")
        private String password;

        @Override
        public final void configure(final WebSecurity web) {
            web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/api/v1/**"
            );
        }

        @Override
        protected final void configure(final AuthenticationManagerBuilder auth) throws Exception {
            auth.ldapAuthentication()
                .userDnPatterns(this.pattern)
                .groupSearchBase(this.group)
                .contextSource()
                .url(this.server)
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute(this.password);
        }

        @Override
        protected final void configure(final HttpSecurity http) throws Exception {
            http.antMatcher("/**")
                .csrf()
                .disable()
                .requestCache()
                .requestCache(new CustomRequestCache())
                .and()
                .authorizeRequests()
                .requestMatchers(WebConfiguration::isFrameworkInternalRequest)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage(WebConfiguration.LOGIN_URL)
                .permitAll()
                .loginProcessingUrl(WebConfiguration.LOGIN_URL)
                .failureUrl(WebConfiguration.LOGIN_FAILURE_URL)
                .and()
                .logout()
                .logoutSuccessUrl(WebConfiguration.LOGIN_URL);
        }

        /**
         * Checks if internal request.
         *
         * @param request Request to check.
         * @return Boolean.
         */
        private static boolean isFrameworkInternalRequest(final HttpServletRequest request) {
            final String param = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
            return Objects.nonNull(param)
                && Stream.of(HandlerHelper.RequestType.values())
                .anyMatch(r -> r.getIdentifier().equals(param));
        }

        /**
         * Custom request cache.
         *
         * @since 1.0
         */
        private static final class CustomRequestCache extends HttpSessionRequestCache {

            @Override
            public void saveRequest(final HttpServletRequest req, final HttpServletResponse res) {
                if (!WebConfiguration.isFrameworkInternalRequest(req)) {
                    super.saveRequest(req, res);
                }
            }
        }
    }
}
