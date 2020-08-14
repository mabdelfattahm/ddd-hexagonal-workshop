/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp.api;

import domain.value.Money;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webapp.Application;

/**
 * Rest API controller.
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/")
public class RestApi {

    /**
     * Create a new account.
     *
     * @param balance Starting balance.
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (4 lines)
     */
    @PostMapping("account")
    public void addAccount(@RequestBody final double balance) {
        Application.createAccount().create(Money.with(balance));
    }
}
