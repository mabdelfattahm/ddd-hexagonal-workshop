/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package domain.value;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Money.
 *
 * @since 1.0
 */
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
public final class Money {

    /**
     * Amount.
     */
    private final BigDecimal amount;

    /**
     * Main constructor.
     *
     * @param amount Amount.
     */
    private Money(final BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Create money instance.
     *
     * @param value Double value.
     * @return Money.
     * @since 1.0
     */
    public static Money with(final double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    /**
     * Is the money positive or zero.
     *
     * @return Boolean
     * @since 1.0
     */
    public boolean isPositiveOrZero() {
        return this.amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Subtract the passed money from the current money.
     *
     * @param money Another money.
     * @return A new money instance.
     * @since 1.0
     */
    public Money minus(final Money money) {
        return new Money(this.amount.subtract(money.amount));
    }

    /**
     * Amount of money.
     *
     * @return Double.
     * @since 1.0
     */
    public Double value() {
        return this.amount.doubleValue();
    }

    /**
     * Add the passed money to the current money.
     *
     * @param money Another money.
     * @return A new money instance.
     * @since 1.0
     */
    public Money plus(final Money money) {
        return new Money(this.amount.add(money.amount));
    }

    @Override
    @SuppressWarnings("PMD.ConfusingTernary")
    public boolean equals(final Object another) {
        final boolean result;
        if (another == this) {
            result = true;
        } else if (!(another instanceof Money)) {
            result = false;
        } else {
            final Money other = (Money) another;
            result = Objects.equals(this.amount, other.amount);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.amount);
    }
}
