package port.out;

import domain.entity.Account;

public interface UpdateAccountActivities {
    void updateActivities(Account account) throws IllegalStateException;
}
