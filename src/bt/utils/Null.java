package bt.utils;

import bt.types.Killable;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Offers utility methods to avoid all kinds of Javas very cool null checks to make code less cluttered.
 *
 * <p>
 * For example this gem
 * </p>
 *
 * <pre>
 * if (value != null)
 * {
 *     value.doSomething();
 * }
 * </pre>
 * <p>
 * Can be converted to
 * </p>
 *
 * <pre>
 * Null.checkRun(value, () -> value.doSomething());
 * </pre>
 *
 * <p>
 * And
 * </p>
 *
 * <pre>
 * if (myList == null)
 * {
 *     myList = new ArrayList();
 * }
 * </pre>
 *
 * <p>
 * Becomes
 * </p>
 *
 * <pre>
 * myList = Null.nullValue(myList, new ArrayList());
 * </pre>
 *
 * @author &#8904
 */
public final class Null
{
    /**
     * Checks if the first parameter is null and if so, returns the second parameter as default value.
     *
     * @param <T>          Type of the values.
     * @param checkValue   The value to be checked for null.
     * @param defaultValue The default value that is returned if the checkValue is null.
     * @return Either the checkValue if it is not null or the defaultValue.
     */
    public static <T> T nullValue(T checkValue, T defaultValue)
    {
        return checkValue == null ? defaultValue : checkValue;
    }

    /**
     * Checks if the first parameter is null and if so, returns the first element of the given default values that is
     * not null.
     *
     * <p>
     * The elements will be checked in the given order.
     * </p>
     *
     * @param <T>           Type of the values.
     * @param checkValue    The value to be checked for null.
     * @param defaultValues The an array of values that will be checked if the checkValue is null. The first element of this that
     *                      is not null will be returned.
     * @return Either the checkValue if it is not null, the first element of the given default values that is not null
     * or null if no non-null values where found.
     */
    public static <T> T nullValue(T checkValue, T... defaultValues)
    {
        if (checkValue != null)
        {
            return checkValue;
        }
        else
        {
            for (T element : defaultValues)
            {
                if (element != null)
                {
                    return element;
                }
            }

            return null;
        }
    }

    /**
     * Checks if the first parameters get method returns null and if so, returns the second parameters get method as
     * default value.
     *
     * @param <T>          Type of the values.
     * @param checkValue   The supplier for the value to be checked for null.
     * @param defaultValue The default supplier for the value that is returned if the checkValue is null.
     * @return Either the checkValue if it is not null or the defaultValue.
     */
    public static <T> T nullValue(Supplier<T> checkValue, Supplier<T> defaultValue)
    {
        T value = checkValue.get();

        if (value != null)
        {
            return value;
        }

        return defaultValue.get();
    }

    /**
     * Checks if the first parameters get method returns null and if so, returns the first non null get method value
     * from the given defaultValues array.
     *
     * <p>
     * The elements will be checked in the given order.
     * </p>
     *
     * @param <T>           Type of the values.
     * @param checkValue    The supplier for the value to be checked for null.
     * @param defaultValues The an array of value suppliers whichs get method returns will be checked if the checkValue is null.
     *                      The first element of this that is not null will be returned.
     * @return Either the checkValue if it is not null, the first element of the given default values that is not null
     * or null if no non-null values where found.
     */
    public static <T> T nullValue(Supplier<T> checkValue, Supplier<T>... defaultValues)
    {
        T value = checkValue.get();

        if (value != null)
        {
            return value;
        }
        else
        {
            for (Supplier<T> element : defaultValues)
            {
                value = element.get();

                if (value != null)
                {
                    return value;
                }
            }

            return null;
        }
    }

    /**
     * Checks if the first parameter is null. If it is not null the given {@link Runnable} will be executed.
     *
     * @param <T>        The type of the first value.
     * @param checkValue The value to be checked for null.
     * @param action     The action to excecute if the given checkValue is not null.
     */
    public static <T> void checkRun(T checkValue, Runnable action)
    {
        if (checkValue != null)
        {
            action.run();
        }
    }

    /**
     * Checks if the first parameter is null. If it is not then the <code>value</code> will be passed to the
     * {@link Consumer}.
     *
     * <p>
     * The consumer will also be checked for null via {@link #checkConsume(Consumer, Object)}.
     * </p>
     *
     * @param <T>        The value of the checked parameter.
     * @param <V>        The value of the object that will be passed to the {@link Consumer}.
     * @param checkValue The value to be checked for null.
     * @param value      The value to be passed to the {@link Consumer}.
     * @param consumer   The consumer to receive the value parameter if the checkValue is not null.
     */
    public static <T, V> void checkConsume(T checkValue, V value, Consumer<V> consumer)
    {
        if (checkValue != null)
        {
            Null.checkConsume(consumer, value);
        }
    }

    /**
     * Checks if the {@link Consumer} is null. If it is not then the <code>value</code> will be passed to it.
     *
     * @param <V>        The type of the {@link Consumer} and the value that will be passed to it.
     * @param checkValue The consumer that will be checked for null.
     * @param value      The value that will be passed to the {@link Consumer} if the consumer is not null.
     */
    public static <V> void checkConsume(Consumer<V> checkValue, V value)
    {
        if (checkValue != null)
        {
            checkValue.accept(value);
        }
    }

    /**
     * Checks if the first parameter is null. If it is not then it will be passed to the given {@link Consumer}.
     *
     * <p>
     * The consumer will also be checked for null via {@link #checkConsume(Consumer, Object)}.
     * </p>
     *
     * @param <V>        The type of the consumer and the value that will be passed to it.
     * @param checkValue The value that will be checked for null.
     * @param consumer   The {@link Consumer} that will receive the value.
     */
    public static <V> void checkConsume(V checkValue, Consumer<V> consumer)
    {
        Null.checkConsume(checkValue, checkValue, consumer);
    }

    /**
     * Checks if the first parameter is null. If it is not then the <code>value</code> will be passed to the
     * {@link Function}.
     *
     * <p>
     * The function will also be checked for null via {@link #checkApply(Function, Object)}.
     * </p>
     *
     * @param <T>        The value of the checked parameter.
     * @param <V>        The value of the object that will be passed to the {@link Function}.
     * @param <R>        The return type of the function.
     * @param checkValue The value to be checked for null.
     * @param value      The value to be passed to the {@link Function}.
     * @param function   The function to receive the value parameter if the checkValue is not null.
     * @return The return value of the function or null if the function was not called.
     */
    public static <T, V, R> R checkApply(T checkValue, V value, Function<V, R> function)
    {
        if (checkValue != null)
        {
            return Null.checkApply(function, value);
        }

        return null;
    }

    /**
     * Checks if the first parameter is null. If it is not then the <code>value</code> will be passed to it.
     *
     * @param <V>        The value of the object that will be passed to the {@link Function}.
     * @param <R>        The return type of the function.
     * @param checkValue The function to be checked for null.
     * @param value      The value to be passed to the {@link Function}.
     * @return The return value of the function or null if the function was not called.
     */
    public static <V, R> R checkApply(Function<V, R> checkValue, V value)
    {
        if (checkValue != null)
        {
            return checkValue.apply(value);
        }

        return null;
    }

    /**
     * Checks if the first parameter is null. If it is not then the <code>value</code> will be passed to the given
     * function.
     *
     * <p>
     * The function will also be checked for null via {@link #checkApply(Function, Object)}.
     * </p>
     *
     * @param <V>        The value of the object that will be passed to the {@link Function}.
     * @param <R>        The return type of the function.
     * @param checkValue The function to be checked for null.
     * @param function   The function that the checkValue will be passed to.
     * @return The return value of the function or null if the function was not called.
     */
    public static <V, R> R checkApply(V checkValue, Function<V, R> function)
    {
        return Null.checkApply(checkValue, checkValue, function);
    }

    /**
     * Checks if the given {@link Runnable} is null. If it is not null then the {@link Runnable#run() run} method will
     * be called.
     *
     * <p>
     * This is just a wrapper method of {@link #checkRun(Object, Runnable) checkRun(action, () -> action.run());}.
     * </p>
     *
     * @param action The action to execute.
     */
    public static void checkRun(Runnable action)
    {
        Null.checkRun(action, () -> action.run());
    }

    /**
     * Checks if the given {@link Closeable} is null. If it is not null then the {@link Closeable#close() close} method
     * will be called.
     *
     * @param closeable The instance to close.
     * @throws IOException
     */
    public static void checkClose(Closeable closeable) throws IOException
    {
        if (closeable != null)
        {
            closeable.close();
        }
    }

    /**
     * Checks if the given {@link Collection} is null. If it is not null then the {@link Collection#clear() clear}
     * method will be called.
     *
     * <p>
     * This is just a wrapper method of {@link #checkRun(Object, Runnable) checkRun(collection, () ->
     * collection.clear());}.
     * </p>
     *
     * @param collection The instance to clear.
     */
    public static void checkClear(Collection collection)
    {
        Null.checkRun(collection, () -> collection.clear());
    }

    /**
     * Checks if the given {@link Killable} is null. If it is not null then the {@link Killable#kill() kill} method
     * will be called.
     *
     * @param killable The instance to close.
     */
    public static void checkKill(Killable killable)
    {
        if (killable != null)
        {
            killable.kill();
        }
    }

    /**
     * Checks if the given value is either null or equal to the given checkValue. If yes,
     * then null is returned, else the given value is returned.
     *
     * @param value      The value to check.
     * @param checkValue The value to check against.
     * @param <T>
     * @return null if either the given value is null or the given value is equal to the given checkValue. Else the value is returned.
     */
    public static <T> T nullIf(T value, Object checkValue)
    {
        if (value == null || Objects.equals(value, checkValue))
        {
            return null;
        }
        else
        {
            return value;
        }
    }

    /**
     * Checks if the given value is either null or the given checkFunction returns true. If yes,
     * then null is returned, else the given value is returned.
     *
     * @param value         The value to check.
     * @param checkFunction The function to check.
     * @param <T>
     * @return null if either the given value is null or the given checkFunction returns true. Else the value is returned.
     */
    public static <T> T nullIf(T value, Predicate<T> checkFunction)
    {
        if (value == null || checkFunction.test(value))
        {
            return null;
        }
        else
        {
            return value;
        }
    }

    /**
     * Checks if the given value is either null or the given checkFunction returns true. If yes,
     * then null is returned, else the given value is returned.
     *
     * @param value         The value to check.
     * @param checkFunction The function to check.
     * @param <T>
     * @return null if either the given value is null or the given checkFunction returns true. Else the value is returned.
     */
    public static <T> T nullIf(T value, Supplier<Boolean> checkFunction)
    {
        if (value == null || checkFunction.get())
        {
            return null;
        }
        else
        {
            return value;
        }
    }
}