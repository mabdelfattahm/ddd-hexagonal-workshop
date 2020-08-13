/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package domain.value;

import java.util.Objects;
import java.util.UUID;

/**
 * Account Id.
 *
 * @since 1.0
 */
public final class AccountId {

    /**
     * UUID as Id.
     */
    private final UUID uuid;

    /**
     * Main constructor.
     *
     * @param uuid UUID.
     */
    private AccountId(final UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Create a new account Id.
     *
     * @return Account Id.
     * @since 1.0
     */
    public static AccountId create() {
        return new AccountId(UUID.randomUUID());
    }

    /**
     * Account Id using the uuid string.
     *
     * @param uuid UUID string.
     * @return Account Id.
     * @since 1.0
     */
    public static AccountId with(final String uuid) {
        return new AccountId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return this.uuid.toString();
    }

    @Override
    @SuppressWarnings("PMD.ConfusingTernary")
    public boolean equals(final Object o) {
        final boolean result;
        if (o == this){
            result = true;
        } else if (!(o instanceof AccountId)) {
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
