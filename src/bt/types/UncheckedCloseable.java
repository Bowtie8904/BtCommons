package bt.types;

/**
 * @author &#8904
 *
 */
public interface UncheckedCloseable extends Runnable, AutoCloseable
{
    @Override
    default void run()
    {
        try
        {
            close();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    static UncheckedCloseable wrap(AutoCloseable c)
    {
        return c::close;
    }
}