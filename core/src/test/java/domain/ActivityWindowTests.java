package domain;

import domain.value.AccountId;
import domain.value.Activity;
import domain.value.ActivityWindow;
import domain.value.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class ActivityWindowTests {

    @Test
    void aggregatesMoneyDepositedIntoAccount() {
        final AccountId id = AccountId.create();
        final ActivityWindow window =
            ActivityWindow.with(
                LocalDateTime.now(),
                List.of(
                    Activity.deposit(id, Money.of(200)),
                    Activity.withdraw(id, Money.of(400)),
                    Activity.transfer(id, AccountId.create(), Money.of(400))
                )
            );
        Assertions.assertEquals(200, window.depositedIntoAccount(id).value());
    }

    @Test
    void aggregatesMoneyWithdrawnFromAccount() {
        final AccountId id = AccountId.create();
        final ActivityWindow window =
            ActivityWindow.with(
                LocalDateTime.now(),
                List.of(
                    Activity.deposit(id, Money.of(200)),
                    Activity.withdraw(id, Money.of(400)),
                    Activity.transfer(id, AccountId.create(), Money.of(400))
                )
            );
        Assertions.assertEquals(800, window.withdrawnFromAccount(id).value());
    }

    @Test
    void modifiableWindowCanAddActivities() {
        final AccountId id = AccountId.create();
        final ActivityWindow window =
            ActivityWindow.with(
                LocalDateTime.now(),
                List.of(
                    Activity.deposit(id, Money.of(200)),
                    Activity.withdraw(id, Money.of(400)),
                    Activity.transfer(id, AccountId.create(), Money.of(400))
                )
            );
        Assertions.assertDoesNotThrow(() -> window.addActivity(Activity.deposit(id, Money.of(100))));
    }


    @Test
    void unmodifiableWindowCannotAddActivities() {
        final AccountId id = AccountId.create();
        final ActivityWindow window =
            ActivityWindow.unmodifiable(
                List.of(
                    Activity.deposit(id, Money.of(200)),
                    Activity.withdraw(id, Money.of(400)),
                    Activity.transfer(id, AccountId.create(), Money.of(400))
                )
            );
        Assertions.assertThrows(
            UnsupportedOperationException.class,
            () -> window.addActivity(Activity.deposit(id, Money.of(100)))
        );
    }

}
