package bt.utils;

/**
 * Offers a functional interface to hide a thrown exception.
 *
 * <p>
 * This interface is supposed to be used with the {@link Exceptions} class to avoid unwanted try catch blocks.
 * </p>
 *
 * @author &#8904
 * @param <T>
 */
@FunctionalInterface
public interface ThrowSupplier<T>
{
    public T get() throws Exception;
}