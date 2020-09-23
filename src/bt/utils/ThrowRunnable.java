package bt.utils;

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
public interface ThrowRunnable
{
    public void run() throws Exception;
}