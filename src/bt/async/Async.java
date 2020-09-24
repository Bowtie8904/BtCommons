package bt.async;

/**
 * Enables blocking calls to receive data asynchronously.
 *
 * @author &#8904
 *
 * @param <T>
 *            The type of expected data.
 */
public class Async<T>
{
    private final String id;
    private Data<T> data = null;
    private Object lock = new Object();

    /**
     * Creates a new instance.
     *
     * @param id
     *            A unique ID that is used to match this async and the expected data.
     */
    public Async(String id)
    {
        this.id = id;
        AsyncManager.get().addAsync(this);
    }

    /**
     * Receives the expected data.
     *
     * <p>
     * This call blocks until the expected data is available.
     * </p>
     *
     * @return
     */
    public Data<T> get()
    {
        Thread.currentThread().setName("Return " + this.id);

        if (this.data == null)
        {
            synchronized (this.lock)
            {
                try
                {
                    this.lock.wait();
                }
                catch (InterruptedException e1)
                {}
            }
        }

        return this.data;
    }

    /**
     * Receives the expected data.
     *
     * <p>
     * This call blocks until the expected data is available or until <code>maxWait</code> milliseconds have passed.
     * </p>
     *
     * @param maxWait
     *            The amount of milliseconds to wait before throwing an {@link AsyncException}.
     * @return
     */
    public Data<T> get(long maxWait) throws AsyncException
    {
        Thread.currentThread().setName("Return " + this.id);

        if (this.data == null)
        {
            synchronized (this.lock)
            {
                try
                {
                    this.lock.wait(maxWait);
                }
                catch (InterruptedException e1)
                {}
            }

            if (this.data == null)
            {
                throw new AsyncException("Return (" + this.id + ") has timed out after waiting for " + maxWait + " milliseconds.");
            }
        }

        return this.data;
    }

    /**
     * Sets the data and notifies any blocking get methods that have been waiting.
     *
     * @param data
     */
    public void set(Data<T> data)
    {
        this.data = data;

        synchronized (this.lock)
        {
            this.lock.notifyAll();
        }
    }

    /**
     * Gets the ID associated with this asynchron data request.
     *
     * @return
     */
    public String getID()
    {
        return this.id;
    }
}