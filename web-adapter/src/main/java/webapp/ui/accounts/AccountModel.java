/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp.ui.accounts;

import domain.entity.Account;
import java.time.ZoneOffset;
import java.util.Comparator;

/**
 * Account view Model.
 *
 * @since 1.0
 */
public final class AccountModel {

    /**
     * Domain model account.
     */
    private final Account account;

    /**
     * Main constructor.
     *
     * @param account Account domain model.
     */
    private AccountModel(final Account account) {
        this.account = account;
    }

    /**
     * Create an account view model from account domain model.
     *
     * @param account Account domain model.
     * @return Account view model.
     * @since 1.0
     */
    @SuppressWarnings("PMD.ProhibitPublicStaticMethods")
    public static AccountModel from(final Account account) {
        return new AccountModel(account);
    }

    /**
     * Account Id.
     *
     * @return String
     * @since 1.0
     */
    String accountId() {
        return this.account.accountId().toString();
    }

    /**
     * Account balance.
     *
     * @return String
     * @since 1.0
     */
    String balance() {
        return String.format("%.2f", this.account.balance().value());
    }

    /**
     * Account last activity.
     *
     * @return Activity view model.
     * @since 1.0
     */
    ActivityModel lastActivity() {
        return
            this.account
                .activities()
                .max(Comparator.comparingLong(v -> v.timestamp.toEpochSecond(ZoneOffset.UTC)))
                .map(ActivityModel::from)
                .orElseGet(ActivityModel::empty);
    }
}
