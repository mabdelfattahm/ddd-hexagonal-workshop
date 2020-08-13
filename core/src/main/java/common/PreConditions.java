/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package common;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility class for validation.
 *
 * @since 1.0
 */
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
public final class PreConditions {

    /**
     * Private constructor to prevent initialization.
     */
    private PreConditions() { }

    /**
     * Test a condition that must be met.
     *
     * @param object Object to check.
     * @param condition Condition predicate
     * @param exception Exception supplier.
     * @param <T> Type of the object to check.
     * @param <U> Type of the exception to throw.
     * @throws U If the condition was not met.
     * @since 1.0
     */
    public static <T, U extends Exception> void require(
        final T object,
        final Predicate<T> condition,
        final Supplier<U> exception
    ) throws U {
        if (condition.negate().test(object)) {
            throw exception.get();
        }
    }
}
