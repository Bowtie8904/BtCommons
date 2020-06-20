package bt.utils.exc;

import bt.log.Logger;
import bt.utils.exc.intf.ThrowConsumer;
import bt.utils.exc.intf.ThrowFunction;
import bt.utils.exc.intf.ThrowRunnable;
import bt.utils.exc.intf.ThrowSupplier;

/**
 * @author &#8904
 *
 */
public class Exceptions
{
    /**
     * Calls the {@link ThrowRunnable#run() run} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be wrapped in a {@link RuntimeException} and thrown again unchecked.
     * </p>
     *
     * @param run
     */
    public static void uncheck(ThrowRunnable run)
    {
        try
        {
            run.run();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls the {@link ThrowRunnable#run() run} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be caught and ignored.
     * </p>
     *
     * @param run
     */
    public static void ignoreThrow(ThrowRunnable run)
    {
        try
        {
            run.run();
        }
        catch (Exception e)
        {}
    }

    /**
     * Calls the {@link ThrowRunnable#run() run} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be caught and logged via the {@link Logger#global() global logger} and its
     * {@link Logger#print(Throwable)} method.
     * </p>
     *
     * @param run
     */
    public static void logThrow(ThrowRunnable run)
    {
        try
        {
            run.run();
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }
    }

    /**
     * Calls the {@link ThrowConsumer#accept() accept} method of the given implementation with the given consumable
     * parameter.
     *
     * <p>
     * Any thrown exception will be wrapped in a {@link RuntimeException} and thrown again unchecked.
     * </p>
     *
     * @param consumer
     */
    public static <T> void uncheck(ThrowConsumer<T> consumer, T consumable)
    {
        try
        {
            consumer.accept(consumable);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls the {@link ThrowConsumer#accept() accept} method of the given implementation with the given consumable
     * parameter.
     *
     * <p>
     * Any thrown exception will be caught and ignored.
     * </p>
     *
     * @param <T>
     *
     * @param consumer
     */
    public static <T> void ignoreThrow(ThrowConsumer<T> consumer, T consumable)
    {
        try
        {
            consumer.accept(consumable);
        }
        catch (Exception e)
        {}
    }

    /**
     * Calls the {@link ThrowConsumer#accept() accept} method of the given implementation with the given
     * consumable parameter.
     *
     * <p>
     * Any thrown exception will be caught and logged via the {@link Logger#global() global logger} and its
     * {@link Logger#print(Throwable)} method.
     * </p>
     *
     * @param <T>
     *
     * @param consumer
     */
    public static <T> void logThrow(ThrowConsumer<T> consumer, T consumable)
    {
        try
        {
            consumer.accept(consumable);
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }
    }

    /**
     * Calls the {@link ThrowSupplier#get() get} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be wrapped in a {@link RuntimeException} and thrown again unchecked.
     * </p>
     *
     * @param supplier
     */
    public static <T> T uncheckGet(ThrowSupplier<T> supplier)
    {
        try
        {
            return supplier.get();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls the {@link ThrowSupplier#get() get} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be caught and ignored.
     * </p>
     *
     * @param <T>
     *
     * @param supplier
     */
    public static <T> T ignoreThrowGet(ThrowSupplier<T> supplier)
    {
        try
        {
            return supplier.get();
        }
        catch (Exception e)
        {}

        return null;
    }

    /**
     * Calls the {@link ThrowSupplier#get() get} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be caught and logged via the {@link Logger#global() global logger} and its
     * {@link Logger#print(Throwable)} method.
     * </p>
     *
     * @param <T>
     *
     * @param supplier
     */
    public static <T> T logThrowGet(ThrowSupplier<T> supplier)
    {
        try
        {
            return supplier.get();
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }

        return null;
    }

    /**
     * Calls the {@link ThrowFunction#apply() apply} method of the given implementation with the given value parameter.
     *
     * <p>
     * Any thrown exception will be wrapped in a {@link RuntimeException} and thrown again unchecked.
     * </p>
     *
     * @param function
     */
    public static <T, K> T uncheckGet(ThrowFunction<T, K> function, K value)
    {
        try
        {
            return function.apply(value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls the {@link ThrowFunction#apply() apply} method of the given implementation with the given value parameter.
     *
     * <p>
     * Any thrown exception will be caught and ignored.
     * </p>
     *
     * @param <T>
     *
     * @param function
     */
    public static <T, K> T ignoreThrowGet(ThrowFunction<T, K> function, K value)
    {
        try
        {
            return function.apply(value);
        }
        catch (Exception e)
        {}

        return null;
    }

    /**
     * Calls the {@link ThrowFunction#apply() apply} method of the given implementation with the given value
     * parameter.
     *
     * <p>
     * Any thrown exception will be caught and logged via the {@link Logger#global() global logger} and its
     * {@link Logger#print(Throwable)} method.
     * </p>
     *
     * @param <T>
     *
     * @param function
     */
    public static <T, K> T logThrowGet(ThrowFunction<T, K> function, K value)
    {
        try
        {
            return function.apply(value);
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }

        return null;
    }
}