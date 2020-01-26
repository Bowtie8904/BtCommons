package bt.utils.nulls;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import bt.runtime.Killable;
import bt.utils.log.Logger;

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
 * Null.check(value, value::doSomething);
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
     * @param <T>
     *            Type of the values.
     * @param checkValue
     *            The value to be checked for null.
     * @param defaultValue
     *            The default value that is returned if the checkValue is null.
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
     * @param <T>
     *            Type of the values.
     * @param checkValue
     *            The value to be checked for null.
     * @param defaultValues
     *            The an array of values that will be checked if the checkValue is null. The first element of this that
     *            is not null will be returned.
     * @return Either the checkValue if it is not null, the first element of the given default values that is not null
     *         or null if no non-null values where found.
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
     * Checks if the first parameter is null. If it is not null the given {@link Runnable} will be executed.
     *
     * @param <T>
     *            The type of the first value.
     * @param checkValue
     *            The value to be checked for null.
     * @param action
     *            The action to excecute if the given checkValue is not null.
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
     * @param <T>
     *            The value of the checked parameter.
     * @param <V>
     *            The value of the object that will be passed to the {@link Consumer}.
     * @param checkValue
     *            The value to be checked for null.
     * @param value
     *            The value to be passed to the {@link Consumer}.
     * @param consumer
     *            The consumer to receive the value parameter if the checkValue is not null.
     */
    public static <T, V> void checkConsume(T checkValue, V value, Consumer<V> consumer)
    {
        if (checkValue != null)
        {
            checkConsume(consumer, value);
        }
    }

    /**
     * Checks if the {@link Consumer} is null. If it is not then the <code>value</code> will be passed to it.
     *
     * @param <V>
     *            The type of the {@link Consumer} and the value that will be passed to it.
     * @param checkValue
     *            The consumer that will be checked for null.
     * @param value
     *            The value that will be passed to the {@link Consumer} if the consumer is not null.
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
     * @param <V>
     *            The type of the consumer and the value that will be passed to it.
     * @param checkValue
     *            The value that will be checked for null.
     * @param consumer
     *            The {@link Consumer} that will receive the value.
     */
    public static <V> void checkConsume(V checkValue, Consumer<V> consumer)
    {
        checkConsume(checkValue, checkValue, consumer);
    }

    /**
     * Checks if the first parameter is null. If it is not then the <code>value</code> will be passed to the
     * {@link Function}.
     *
     * <p>
     * The function will also be checked for null via {@link #checkApply(Function, Object)}.
     * </p>
     *
     * @param <T>
     *            The value of the checked parameter.
     * @param <V>
     *            The value of the object that will be passed to the {@link Function}.
     * @param <R>
     *            The return type of the function.
     * @param checkValue
     *            The value to be checked for null.
     * @param value
     *            The value to be passed to the {@link Function}.
     * @param function
     *            The function to receive the value parameter if the checkValue is not null.
     *
     * @return The return value of the function or null if the function was not called.
     */
    public static <T, V, R> R checkApply(T checkValue, V value, Function<V, R> function)
    {
        if (checkValue != null)
        {
            return checkApply(function, value);
        }

        return null;
    }

    /**
     * Checks if the first parameter is null. If it is not then the <code>value</code> will be passed to it.
     *
     * @param <V>
     *            The value of the object that will be passed to the {@link Function}.
     * @param <R>
     *            The return type of the function.
     * @param checkValue
     *            The function to be checked for null.
     * @param value
     *            The value to be passed to the {@link Function}.
     *
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
     * @param <V>
     *            The value of the object that will be passed to the {@link Function}.
     * @param <R>
     *            The return type of the function.
     * @param checkValue
     *            The function to be checked for null.
     * @param function
     *            The function that the checkValue will be passed to.
     *
     * @return The return value of the function or null if the function was not called.
     */
    public static <V, R> R checkApply(V checkValue, Function<V, R> function)
    {
        return checkApply(checkValue, checkValue, function);
    }

    /**
     * Checks if the given {@link Runnable} is null. If it is not null then the {@link Runnable#run() run} method will
     * be called.
     *
     * <p>
     * This is just a wrapper method of {@link #checkRun(Object, Runnable) nullCheck(action, action::run);}.
     * </p>
     *
     * @param action
     *            The action to execute.
     */
    public static void checkRun(Runnable action)
    {
        checkRun(action, action::run);
    }

    /**
     * Checks if the given {@link Closeable} is null. If it is not null then the {@link Closeable#close() close} method
     * will be called. The possible {@link IOException} will be caught and logged to {@link Logger#global()}.
     *
     * <p>
     * This is just a wrapper method of {@link #checkRun(Object, Runnable) nullCheck(closeable, closeable::close);}.
     * </p>
     *
     * @param closeable
     *            The instance to close.
     */
    public static void checkClose(Closeable closeable)
    {
        checkRun(closeable, () ->
        {
            try
            {
                closeable.close();
            }
            catch (IOException e1)
            {
                Logger.global().print(e1);
            }
        });
    }

    /**
     * Checks if the given {@link Killable} is null. If it is not null then the {@link Killable#kill() kill} method will
     * be called.
     *
     * <p>
     * This is just a wrapper method of {@link #checkRun(Object, Runnable) nullCheck(killable, killable::kill);}.
     * </p>
     *
     * @param killable
     *            The instance to kill.
     */
    public static void checkKill(Killable killable)
    {
        checkRun(killable, killable::kill);
    }

    /**
     * Checks if the given {@link Collection} is null. If it is not null then the {@link Collection#clear() clear}
     * method will be called.
     *
     * <p>
     * This is just a wrapper method of {@link #checkRun(Object, Runnable) nullCheck(collection, collection::clear);}.
     * </p>
     *
     * @param collection
     *            The instance to clear.
     */
    public static void checkClear(Collection collection)
    {
        checkRun(collection, collection::clear);
    }
}