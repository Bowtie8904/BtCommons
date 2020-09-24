package bt.async;

import java.util.concurrent.ConcurrentHashMap;

import bt.types.Singleton;

/**
 * Manages waiting {@link Async} and incoming {@link Data} to match them together.
 *
 * @author &#8904
 */
public class AsyncManager
{
    private ConcurrentHashMap<String, Async> asyncs;
    private ConcurrentHashMap<String, Data> datapool;

    public static AsyncManager get()
    {
        return Singleton.of(AsyncManager.class);
    }

    protected AsyncManager()
    {
        this.datapool = new ConcurrentHashMap<>();
        this.asyncs = new ConcurrentHashMap<>();
    }

    /**
     * Adds the given {@link Data} to the pool. If an {@link Async} object with the same ID as the data is known it will
     * be notified about the available data.
     *
     * @param data
     */
    public synchronized void addData(Data data)
    {
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
}