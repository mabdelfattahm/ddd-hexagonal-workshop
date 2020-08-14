/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package postgres;

import domain.entity.Account;
import domain.value.AccountId;
import domain.value.Activity;
import domain.value.ActivityWindow;
import domain.value.Money;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Account entity.
 *
 * @since 1.0
 */
public final class PgAccount {

    /**
     * Account Id.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final String id;

    /**
     * Starting balance.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final Double balance;

    /**
     * Activities list.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    final List<PgActivity> activities;

    /**
     * Main constructor.
     *
     * @param id Account Id.
     * @param balance Starting balance.
     * @param activities Activities list.
     * @since 1.0
     */
    public PgAccount(final String id, final Double balance, final List<PgActivity> activities) {
        this.id = id;
        this.balance = balance;
        this.activities = activities;
    }

    /**
     * Convert to domain account.
     *
     * @return Account domain model.
     */
    public Account toDomain() {
        final List<Activity> list =
            this.activities
                .stream()
                .filter(PgActivity::selfValidate)
                .map(PgActivity::toDomain)
                .collect(Collectors.toList());
        final AccountId account = AccountId.with(this.id);
        final ActivityWindow window = ActivityWindow.with(LocalDateTime.now(), list);
        return Account.with(account, Money.with(this.balance), window);
    }
}
