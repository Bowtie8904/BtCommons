package bt.async;

import bt.log.Log;
import bt.utils.Null;

import java.util.function.Consumer;

/**
 * Enables blocking calls to receive data asynchronously.
 *
 * @param <T> The type of expected data.
 *
 * @author &#8904
 */
public class Async<T>
{
    private final String id;
    private Data<T> data = null;
    private Object lock = new Object();
    private long addTime;
    private Thread thread;
    private boolean removedFromManager;
    private Consumer<T> dataConsumer;

    /**
     * Creates a new instance.
     *
     * @param id A unique ID that is used to match this async and the expected data.
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
        Log.entry();
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

        T ret = this.data.get();

        Log.exit(ret);

        return ret;
    }

    /**
     * Receives the expected data.
     *
     * <p>
     * This call blocks until the expected data is available or until <code>maxWait</code> milliseconds have passed.
     * </p>
     *
     * @param maxWait The amount of milliseconds to wait before throwing an {@link AsyncException}.
     *
     * @return
     */
    public T get(long maxWait) throws AsyncException
    {
        Log.entry(maxWait);
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

        T ret = this.data.get();

        Log.exit(ret);

        return ret;
    }

    public synchronized void onReceive(Consumer<T> dataConsumer)
    {
        Log.entry(dataConsumer);
        this.dataConsumer = dataConsumer;

        if (this.data != null)
        {
            Null.checkConsume(this.dataConsumer, this.data.get());
        }

        Log.exit();
    }

    public void removedFromManager()
    {
        Log.entry();
        this.removedFromManager = true;

        if (this.thread != null)
        {
            this.thread.interrupt();
        }

        Log.exit();
    }

    /**
     * Sets the data and notifies any blocking get methods that have been waiting.
     *
     * @param data
     */
    public synchronized void set(Data<T> data)
    {
        Log.entry(data);

        this.data = data;

        Null.checkConsume(this.dataConsumer, this.data.get());

        synchronized (this.lock)
        {
            this.lock.notifyAll();
        }

        Log.exit();
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
     * @param addTime the addTime to set
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