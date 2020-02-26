package bt.utils.exc;

import bt.utils.exc.intf.IgnoreThrowConsumer;
import bt.utils.exc.intf.IgnoreThrowRunnable;
import bt.utils.log.Logger;

/**
 * @author &#8904
 *
 */
public class Exceptions
{
    /**
     * Calls the {@link IgnoreThrowRunnable#run() run} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be caught and ignored.
     * </p>
     *
     * @param run
     */
    public static void ignoreThrow(IgnoreThrowRunnable run)
    {
        try
        {
            run.run();
        }
        catch (Exception e)
        {}
    }

    /**
     * Calls the {@link IgnoreThrowRunnable#run() run} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be caught and logged via the {@link Logger#global() global logger} and its
     * {@link Logger#print(Throwable)} method.
     * </p>
     *
     * @param run
     */
    public static void logThrow(IgnoreThrowRunnable run)
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
     * Calls the {@link IgnoreThrowConsumer#accept() accept} method of the given implementation with the given
     * consumable parameter.
     *
     * <p>
     * Any thrown exception will be caught and ignored.
     * </p>
     *
     * @param <T>
     *
     * @param consumer
     */
    public static <T> void ignoreThrow(IgnoreThrowConsumer<T> consumer, T consumable)
    {
        try
        {
            consumer.accept(consumable);
        }
        catch (Exception e)
        {}
    }

    /**
     * Calls the {@link IgnoreThrowConsumer#accept() accept} method of the given implementation with the given
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
    public static <T> void logThrow(IgnoreThrowConsumer<T> consumer, T consumable)
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
}