package bt.utils.exc.intf;

import bt.utils.exc.Exceptions;

/**
 * Offers a functional interface to hide a thrown exception.
 *
 * <p>
 * This interface is supposed to be used with the {@link Exceptions} class to avoid unwanted try catch blocks.
 * </p>
 *
 * @author &#8904
 */
@FunctionalInterface
public interface IgnoreThrowRunnable
{
    public void run() throws Exception;
}