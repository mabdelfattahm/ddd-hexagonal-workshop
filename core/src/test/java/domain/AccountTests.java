/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package domain;

import domain.entity.Account;
import domain.value.AccountId;
import domain.value.Activity;
import domain.value.ActivityWindow;
import domain.value.Money;
import exception.InsufficientFundsException;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Account domain model tests.
 *
 * @since 1.0
 */
public class AccountTests {

    /**
     * Test balance calculations.
     *
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (3 lines)
     */
    @Test
    void calculatesBalance() {
        final AccountId id = AccountId.create();
        final Account account =
            Account.with(
                id,
                Money.with(1000),
                ActivityWindow.with(
                    LocalDateTime.now(),
                    Arrays.asList(
                        Activity.deposit(id, Money.with(200)),
                        Activity.withdraw(id, Money.with(400))
                    )
                )
            );
        // @checkstyle MagicNumber (1 line)
        Assertions.assertEquals(800, account.balance().value());
    }

    /**
     * Test withdrawal.
     *
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (3 lines)
     */
    @Test
    void withdrawalSucceeds() {
        final AccountId id = AccountId.create();
        final Account account =
            Account.with(
                id,
                Money.with(1000),
                ActivityWindow.with(
                    LocalDateTime.now(),
                    Arrays.asList(
                        Activity.deposit(id, Money.with(200)),
                        Activity.withdraw(id, Money.with(400))
                    )
                )
            );
        // @checkstyle MagicNumber (1 line)
        Assertions.assertDoesNotThrow(() -> account.withdraw(Money.with(800)));
        // @checkstyle MagicNumber (1 line)
        Assertions.assertEquals(0, account.balance().value());
    }

    /**
     * Test deposition.
     *
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (3 lines)
     */
    @Test
    void withdrawalFails() {
        final AccountId id = AccountId.create();
        final Account account =
            Account.with(
                id,
                Money.with(1000),
                ActivityWindow.with(
                    LocalDateTime.now(),
                    Arrays.asList(
                        Activity.deposit(id, Money.with(200)),
                        Activity.withdraw(id, Money.with(400))
                    )
                )
            );
        // @checkstyle MagicNumber (3 line)
        Assertions.assertThrows(
            InsufficientFundsException.class,
            () -> account.withdraw(Money.with(1000))
        );
        // @checkstyle MagicNumber (1 line)
        Assertions.assertEquals(800, account.balance().value());
    }
}
