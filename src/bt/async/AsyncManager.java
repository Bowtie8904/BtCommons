package bt.async;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import bt.scheduler.Threads;
import bt.types.Singleton;

/**
 * Manages waiting {@link Async} and incoming {@link Data} to match them together.
 *
 * @author &#8904
 */
public class AsyncManager
{
    private volatile ConcurrentHashMap<String, Async> asyncs;
    private volatile ConcurrentHashMap<String, Data> datapool;
    private long timeBeforeCleanUp = 600000;

    public static AsyncManager get()
    {
        return Singleton.of(AsyncManager.class);
    }

    protected AsyncManager()
    {
        this.datapool = new ConcurrentHashMap<>();
        this.asyncs = new ConcurrentHashMap<>();
        Threads.get().scheduleAtFixedRateDaemon(this::cleanUpUnsatisfiedEntries, 3, 3, TimeUnit.SECONDS);
    }

    /**
     * Adds the given {@link Data} to the pool. If an {@link Async} object with the same ID as the data is known it will
     * be notified about the available data.
     *
     * @param data
     */
    public synchronized void addData(Data data)
    {
        data.setAddTime(System.currentTimeMillis());
        Async async = this.asyncs.get(data.getID());

        if (async != null)
        {
            async.set(data);
            this.asyncs.remove(data.getID());
        }
        else
        {
            this.datapool.put(data.getID(), data);
        }
    }

    /**
     * Adds the given {@link Async} to the pool. If a {@link Data} object with the same ID is known then the given
     * {@link Async} will be notified immediately.
     *
     * @param async
     */
    public synchronized void addAsync(Async async)
    {
        async.setAddTime(System.currentTimeMillis());
        Data data = this.datapool.get(async.getID());

        if (data != null)
        {
            async.set(data);
            this.datapool.remove(data.getID());
        }
        else
        {
            this.asyncs.put(async.getID(), async);
        }
    }

    private synchronized void cleanUpUnsatisfiedEntries()
    {
        for (var async : this.asyncs.values())
        {
            if (async.getAddTime() + this.timeBeforeCleanUp <= System.currentTimeMillis())
            {
                this.asyncs.remove(async.getID());
                async.removedFromManager();
            }
        }

        for (var data : this.datapool.values())
        {
            if (data.getAddTime() + this.timeBeforeCleanUp <= System.currentTimeMillis())
            {
                this.datapool.remove(data.getID());
            }
        }
    }

    /**
     * @return the timeBeforeCleanUp
     */
    public long getTimeBeforeCleanUp()
    {
        return this.timeBeforeCleanUp;
    }

    /**
     * @param timeBeforeCleanUp
     *            the timeBeforeCleanUp to set
     */
    public void setTimeBeforeCleanUp(long timeBeforeCleanUp)
    {
        this.timeBeforeCleanUp = timeBeforeCleanUp;
    }
}