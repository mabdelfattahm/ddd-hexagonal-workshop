package domain.value;

import java.util.Objects;
import java.util.UUID;

public final class AccountId {

    private final UUID uuid;

    private AccountId(UUID uuid) {
        this.uuid = uuid;
    }

    public static AccountId create() {
        return new AccountId(UUID.randomUUID());
    }

    public static AccountId of(String uuid) {
        return new AccountId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return this.uuid.toString();
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (!(o instanceof AccountId)) {
            result = false;
        } else {
            final AccountId other = (AccountId) o;
            result = Objects.equals(this.uuid, other.uuid);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid);
    }
}
