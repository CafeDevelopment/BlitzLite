package club.cafedevelopment.blitzlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Reap
 *
 * Put this over {@link java.util.function.Consumer}s you want to count as listeners.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BlitzEntry {
    /**
     * @return the type of the event listener. Has to be identical to the Type Parameter on the {@link java.util.function.Consumer}.
     */
    Class<?> type();

    /**
     * @return the priority for the event. Is 0 by default to have {@link Integer#MIN_VALUE} as the minimum and {@link Integer#MAX_VALUE} as the maximum.
     */
    int priority() default 0;
}
