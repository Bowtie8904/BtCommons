package bt.utils.exc;

import bt.utils.exc.intf.IgnoreThrowCaller;
import bt.utils.log.Logger;

/**
 * @author &#8904
 *
 */
public class Exceptions
{
    /**
     * Calls the {@link IgnoreThrowCaller#call() call} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be caught and ignored.
     * </p>
     *
     * @param call
     */
    public static void ignoreThrow(IgnoreThrowCaller call)
    {
        try
        {
            call.call();
        }
        catch (Exception e)
        {}
    }

    /**
     * Calls the {@link IgnoreThrowCaller#call() call} method of the given implementation.
     *
     * <p>
     * Any thrown exception will be caught and logged via the {@link Logger#global() global logger} and its
     * {@link Logger#print(Throwable)} method.
     * </p>
     *
     * @param call
     */
    public static void logThrow(IgnoreThrowCaller call)
    {
        try
        {
            call.call();
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }
    }
}