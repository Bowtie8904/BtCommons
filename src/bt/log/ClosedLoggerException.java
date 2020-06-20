package bt.log;

/**
 * Thrown when a print method of a closed {@link Log} instance is called.
 * 
 * @author &#8904
 */
public class ClosedLoggerException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * Create a new exception with a detail message.
     * 
     * @param message
     *            The message describing the cause of this exception.
     */
    protected ClosedLoggerException(String message)
    {
        super(message);
    }

    /**
     * Create a new exception with a detail message.
     * 
     * @param message
     *            The message describing the cause of this exception.
     * @param e
     *            The exception causing this exception to be thrown.
     */
    protected ClosedLoggerException(String message, Throwable e)
    {
        super(message,
              e);
    }
}