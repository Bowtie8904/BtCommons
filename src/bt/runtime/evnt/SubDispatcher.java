package bt.runtime.evnt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Dispatches data of a specific type to all subscribers.
 * 
 * @author &#8904
 * @param <T>
 *            The type of data that this instance will dispatch.
 */
public class SubDispatcher<T>
{
    /** A list of {@link Consumer} subscribers. */
    private List<Consumer<T>> consumers;

    /** A list of {@link Runnable} subscribers. */
    private List<Runnable> runnables;

    /**
     * Creates a new instance.
     */
    protected SubDispatcher()
    {
        this.consumers = new ArrayList<>();
        this.runnables = new ArrayList<>();
    }

    /**
     * Adds the given consumer to the list of subscribers. That means that the consumer's accept method will be called
     * when this SubDispatcher dispatches data via {@link #dispatch(Object)}.
     * 
     * @param consumer
     *            The new subscriber.
     */
    protected void subscribe(Consumer<T> consumer)
    {
        this.consumers.add(consumer);
    }

    /**
     * Removes the given consumer from the list of subscribers.
     * 
     * <p>
     * After this is called, the given subscriber will no longer receive data via the {@link #dispatch(Object)} method.
     * </p>
     * 
     * @param consumer
     *            The subscriber to remove.
     * @return true if the given consumer was subscribed.
     */
    protected boolean unsubscribe(Consumer<T> consumer)
    {
        return this.consumers.remove(consumer);
    }

    /**
     * Adds the given runnable to the list of subscribers. That means that the runnable's run method will be called when
     * this SubDispatcher dispatches data via {@link #dispatch(Object)}.
     * 
     * @param runnable
     *            The new subscriber.
     */
    protected void subscribe(Runnable runnable)
    {
        this.runnables.add(runnable);
    }

    /**
     * Removes the given runnable from the list of subscribers.
     * 
     * <p>
     * After this is called, the given subscriber will no longer be executed via the {@link #dispatch(Object)} method.
     * </p>
     * 
     * @param runnable
     *            The subscriber to remove.
     * @return true if the given runnable was subscribed.
     */
    protected boolean unsubscribe(Runnable runnable)
    {
        return this.runnables.remove(runnable);
    }

    /**
     * Dispatches the given data to all subscribers.
     * 
     * <p>
     * The {@link Consumer} subscribers will be executed before the {@link Runnable} ones.
     * </p>
     * 
     * @param data
     *            The object to dispatch.
     * 
     * @return The number of subscribers that received the data.
     */
    protected int dispatch(T data)
    {
        for (var consumer : this.consumers)
        {
            consumer.accept(data);
        }

        for (var runnable : this.runnables)
        {
            runnable.run();
        }

        return this.consumers.size() + this.runnables.size();
    }

    /**
     * Gets a list containing all subscribers of this instance.
     * 
     * <p>
     * All elements in the list will be {@link Consumer}s. Any {@link Runnable} subscriber will be wrapped in a new
     * Consumer.
     * </p>
     * 
     * @return The list of subscribers.
     */
    public List<Consumer<T>> getSubscribers()
    {
        var list = new ArrayList<Consumer<T>>();
        list.addAll(this.consumers);
        this.runnables.forEach(r -> list.add((d) -> r.run()));

        return list;
    }
}