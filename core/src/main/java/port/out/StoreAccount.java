package port.out;

import domain.entity.Account;

public interface StoreAccount {
    void store(Account account) throws IllegalStateException;
}
