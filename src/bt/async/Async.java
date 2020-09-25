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
    private long addTime;
    private Thread thread;
    private boolean removedFromManager;

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
    public T get() throws AsyncException
    {
        Thread.currentThread().setName("Return " + this.id);
        this.thread = Thread.currentThread();

        if (this.data == null)
        {
            synchronized (this.lock)
            {
                try
                {
                    this.lock.wait();
                }
                catch (InterruptedException e1)
                {
                    throw new AsyncException("Async (" + this.id + ") was removed from the AsyncManager and will not receive any Data.");
                }
            }
        }
        else if (this.removedFromManager)
        {
            throw new AsyncException("Async (" + this.id + ") was removed from the AsyncManager and will not receive any Data.");
        }

        return this.data.get();
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
    public T get(long maxWait) throws AsyncException
    {
        Thread.currentThread().setName("Return " + this.id);
        this.thread = Thread.currentThread();

        if (this.data == null)
        {
            synchronized (this.lock)
            {
                try
                {
                    this.lock.wait(maxWait);
                }
                catch (InterruptedException e1)
                {
                    throw new AsyncException("Async (" + this.id + ") was removed from the AsyncManager and will not receive any Data.");
                }
            }

            if (this.data == null)
            {
                throw new AsyncException("Async (" + this.id + ") has timed out after waiting for " + maxWait + " milliseconds.");
            }
        }
        else if (this.removedFromManager)
        {
            throw new AsyncException("Async (" + this.id + ") was removed from the AsyncManager and will not receive any Data.");
        }

        return this.data.get();
    }

    public void removedFromManager()
    {
        this.removedFromManager = true;

        if (this.thread != null)
        {
            this.thread.interrupt();
        }
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

    /**
     * @return the addTime
     */
    public long getAddTime()
    {
        return this.addTime;
    }

    /**
     * @param addTime
     *            the addTime to set
     */
    public void setAddTime(long addTime)
    {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }
        else if (this.id != null && this.id.equals(o))
        {
            return true;
        }
        else if (this.id != null && o instanceof Async && this.id.equals(((Async)o).getID()))
        {
            return true;
        }

        return false;
    }
}