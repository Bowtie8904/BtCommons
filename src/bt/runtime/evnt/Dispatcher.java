package bt.runtime.evnt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import bt.types.number.MutableInt;
import bt.utils.Null;

/**
 * A basic data dispatcher for generic dispatching of i.e. events to listeners.
 *
 * @author &#8904
 *
 */
public class Dispatcher
{
    /** A map of class (type of the data that is dispatched) to SubDispatcher. */
    private Map<Class, SubDispatcher> subDispatchers;

    /**
     * Creates a new instance.
     */
    public Dispatcher()
    {
        this.subDispatchers = new ConcurrentHashMap<>();
    }

    /**
     * Subscribes the given consumer implementation to the given data type.
     *
     * <p>
     * If the given data type is {@link #dispatch}ed by this instance, all consumers that are subscribed to the given
     * type are executed and the dispatched data is passed.
     * </p>
     *
     * @param type
     * @param listener
     */
    public <T> void subscribeTo(Class<T> type, Consumer<T> consumer)
    {
        if (!this.subDispatchers.containsKey(type))
        {
            this.subDispatchers.put(type,
                                    new SubDispatcher<>(type));
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        dispatcher.subscribe(consumer);
    }

    /**
     * Unsubscribes the given comsumer from receiving data of the given type.
     *
     * @param type
     * @param listener
     *
     * @return true if the given consumer was subscribed.
     */
    public <T> boolean unsubscribeFrom(Class<T> type, Consumer<T> consumer)
    {
        if (!this.subDispatchers.containsKey(type))
        {
            return false;
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        return dispatcher.unsubscribe(consumer);
    }

    /**
     * Subscribes the given runnable implementation to the given data type.
     *
     * <p>
     * If the given data type is {@link #dispatch}ed by this instance, all subscribers of the given type are executed
     * and the dispatched data is passed.
     * </p>
     *
     * @param type
     * @param listener
     */
    public <T> void subscribeTo(Class<T> type, Runnable runnable)
    {
        if (!this.subDispatchers.containsKey(type))
        {
            this.subDispatchers.put(type,
                                    new SubDispatcher<>(type));
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        dispatcher.subscribe(runnable);
    }

    /**
     * Unsubscribes the given runnable from receiving data of the given type.
     *
     * @param type
     * @param listener
     *
     * @return true if the given runnable was subscribed.
     */
    public <T> boolean unsubscribeFrom(Class<T> type, Runnable runnable)
    {
        if (!this.subDispatchers.containsKey(type))
        {
            return false;
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        return dispatcher.unsubscribe(runnable);
    }

    /**
     * Dispatches the given data to all instances that are subscribed to that specific data type or any of its super
     * types.
     *
     * @param data
     * @return The number of subscribers that received the data.
     */
    public <T> int dispatch(T data)
    {
        MutableInt dispatchCount = new MutableInt(0);

        this.subDispatchers.values()
                           .stream()
                           .filter(d -> d.getType().isInstance(data))
                           .forEach(d -> dispatchCount.add(d.dispatch(data)));

        return dispatchCount.get();
    }

    /**
     * Gets a list containing all subscribers of this instance that are subscribed to the given data type.
     *
     * <p>
     * All elements in the list will be {@link Consumer}s. Any {@link Runnable} subscriber will be wrapped in a new
     * Consumer.
     * </p>
     *
     * @return The list of subscribers.
     */
    public <T> List<Consumer<T>> getSubscribers(Class<T> type)
    {
        if (!this.subDispatchers.containsKey(type))
        {
            return new ArrayList<>();
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        return dispatcher.getSubscribers();
    }

    /**
     * Creals all subscribers to the given type.
     *
     * @param <T>
     * @param type
     */
    public <T> void clear(Class<T> type)
    {
        var subDispatcher = this.subDispatchers.get(type);
        Null.checkRun(subDispatcher, subDispatcher::clear);
    }
}