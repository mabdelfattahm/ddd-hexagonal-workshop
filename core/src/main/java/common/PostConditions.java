/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package common;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 */
public class PostConditions {
    public static <T, U extends Exception> void must(T object, Predicate<T> predicate, Supplier<U> supplier) throws U {
        if(predicate.negate().test(object)){
            throw supplier.get();
        }
    }
}
