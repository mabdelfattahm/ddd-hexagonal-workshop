package domain;

import domain.entity.Account;
import domain.value.AccountId;
import domain.value.Activity;
import domain.value.ActivityWindow;
import domain.value.Money;
import exception.InsufficientFundsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class AccountTests {

    @Test
    void calculatesBalance() {
        final AccountId id = AccountId.create();
        final Account account =
            Account.of(
                id,
                Money.of(1000),
                ActivityWindow.with(
                    LocalDateTime.now(),
                    List.of(
                        Activity.deposit(id, Money.of(200)),
                        Activity.withdraw(id, Money.of(400))
                    )
                )
            );
        Assertions.assertEquals(800, account.balance().value());
    }

    @Test
    void withdrawalSucceeds() {
        final AccountId id = AccountId.create();
        final Account account =
            Account.of(
                id,
                Money.of(1000),
                ActivityWindow.with(
                    LocalDateTime.now(),
                    List.of(
                        Activity.deposit(id, Money.of(200)),
                        Activity.withdraw(id, Money.of(400))
                    )
                )
            );
        Assertions.assertDoesNotThrow(
            () -> account.withdraw(Money.of(800))
        );
        Assertions.assertEquals(0, account.balance().value());
    }

    @Test
    void withdrawalFails() {
        final AccountId id = AccountId.create();
        final Account account =
            Account.of(
                id,
                Money.of(1000),
                ActivityWindow.with(
                    LocalDateTime.now(),
                    List.of(
                        Activity.deposit(id, Money.of(200)),
                        Activity.withdraw(id, Money.of(400))
                    )
                )
            );
        Assertions.assertThrows(
            InsufficientFundsException.class,
            () -> account.withdraw(Money.of(1000))
        );
        Assertions.assertEquals(800, account.balance().value());
    }
}
