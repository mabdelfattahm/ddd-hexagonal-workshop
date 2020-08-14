/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp.api;

import domain.value.AccountId;
import domain.value.Money;
import exception.ConcurrentOperationException;
import exception.InsufficientFundsException;
import org.springframework.web.bind.annotation.PathVariable;
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
     * @return Account Id.
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (4 lines)
     */
    @PostMapping("account")
    public String addAccount(@RequestBody final String balance) {
        return
            Application
                .createAccount()
                .create(Money.with(Double.parseDouble(balance)))
                .accountId()
                .toString();
    }

    /**
     * Deposit into an account.
     *
     * @param account Account Id.
     * @param amount Money deposited.
     * @since 1.0
     * @throws InsufficientFundsException If account does not have enough money.
     * @throws ConcurrentOperationException If the account is concurrently being modified.
     * @checkstyle NonStaticMethodCheck (4 lines)
     */
    @PostMapping("account/{id}/deposit")
    public void depositIntoAccount(
        @PathVariable("id") final String account,
        @RequestBody final String amount
    ) throws InsufficientFundsException, ConcurrentOperationException {
        Application
            .sendMoney()
            .deposit(
                AccountId.with(account),
                Money.with(Double.parseDouble(amount))
            );
    }

    /**
     * Withdraw from an account.
     *
     * @param account Account Id.
     * @param amount Money withdrawn.
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (4 lines)
     * @throws InsufficientFundsException If account does not have enough money.
     * @throws ConcurrentOperationException If the account is concurrently being modified.
     */
    @PostMapping("account/{id}/withdraw")
    public void withdrawFromAccount(
        @PathVariable("id") final String account,
        @RequestBody final String amount
    ) throws InsufficientFundsException, ConcurrentOperationException {
        Application
            .sendMoney()
            .withdraw(
                AccountId.with(account),
                Money.with(Double.parseDouble(amount))
            );
    }
}
