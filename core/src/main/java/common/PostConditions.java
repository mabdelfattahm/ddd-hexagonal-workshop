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
public class PostConditions {

    /**
     * Test a condition that must be successful or throw the provided exception.
     *
     * @param object Object to check.
     * @param condition Condition predicate.
     * @param exception Exception supplier.
     * @param <T> Type of object to check.
     * @param <U> Exception type.
     * @throws U If the condition was not met.
     * @since 1.0
     */
    public static <T, U extends Exception> void must(
        final T object,
        final Predicate<T> condition,
        final Supplier<U> exception
    ) throws U {
        if (condition.negate().test(object)) {
            throw exception.get();
        }
    }
}
