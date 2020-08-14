/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package domain;

import domain.value.AccountId;
import domain.value.Activity;
import domain.value.ActivityWindow;
import domain.value.Money;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Activity window domain model tests.
 *
 * @since 1.0
 */
public class ActivityWindowTests {

    /**
     * Test deposition aggregation.
     *
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (3 lines)
     */
    @Test
    void aggregatesMoneyDepositedIntoAccount() {
        final AccountId id = AccountId.create();
        final LocalDateTime now = LocalDateTime.now();
        final ActivityWindow window =
            ActivityWindow.with(
                now,
                Arrays.asList(
                    Activity.deposit(id, Money.with(200)),
                    Activity.withdraw(id, Money.with(400)),
                    Activity.transfer(id, AccountId.create(), Money.with(300))
                )
            );
        // @checkstyle MagicNumber (1 line)
        Assertions.assertEquals(200, window.depositedIntoAccount(id).value());
    }

    /**
     * Test withdrawals aggregation.
     *
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (3 lines)
     */
    @Test
    void aggregatesMoneyWithdrawnFromAccount() {
        final AccountId id = AccountId.create();
        final LocalDateTime now = LocalDateTime.now();
        final ActivityWindow window =
            ActivityWindow.with(
                now,
                Arrays.asList(
                    Activity.deposit(id, Money.with(200)),
                    Activity.withdraw(id, Money.with(400)),
                    Activity.transfer(id, AccountId.create(), Money.with(400))
                )
            );
        // @checkstyle MagicNumber (1 line)
        Assertions.assertEquals(800, window.withdrawnFromAccount(id).value());
    }

    /**
     * Test modifying normal activity window.
     *
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (3 lines)
     */
    @Test
    void modifiableWindowCanAddActivities() {
        final AccountId id = AccountId.create();
        final ActivityWindow window =
            ActivityWindow.with(
                LocalDateTime.now(),
                Arrays.asList(
                    Activity.deposit(id, Money.with(200)),
                    Activity.withdraw(id, Money.with(400)),
                    Activity.transfer(id, AccountId.create(), Money.with(400))
                )
            );
        // @checkstyle MagicNumber (2 lines)
        Assertions.assertDoesNotThrow(
            () -> window.addActivity(Activity.deposit(id, Money.with(100)))
        );
    }

    /**
     * Test modofying an unmodifiable activity window.
     *
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (3 lines)
     */
    @Test
    void unmodifiableWindowCannotAddActivities() {
        final AccountId id = AccountId.create();
        final ActivityWindow window =
            ActivityWindow.unmodifiable(
                Arrays.asList(
                    Activity.deposit(id, Money.with(200)),
                    Activity.withdraw(id, Money.with(400)),
                    Activity.transfer(id, AccountId.create(), Money.with(400))
                )
            );
        // @checkstyle MagicNumber (3 lines)
        Assertions.assertThrows(
            UnsupportedOperationException.class,
            () -> window.addActivity(Activity.deposit(id, Money.with(100)))
        );
    }
}
