package bt.runtime.evnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        this.subDispatchers = new HashMap<>();
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
            this.subDispatchers.put(type, new SubDispatcher<T>());
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        dispatcher.subscribe(consumer);
    }

    /**
     * Unsubscribes the given comsumer from receiving data of the given type.
     * 
     * @param type
     * @param listener
     */
    public <T> void unsubscribeFrom(Class<T> type, Consumer<T> consumer)
    {
        if (!this.subDispatchers.containsKey(type))
        {
            return;
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        dispatcher.unsubscribe(consumer);
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
            this.subDispatchers.put(type, new SubDispatcher<T>());
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        dispatcher.subscribe(runnable);
    }

    /**
     * Unsubscribes the given runnable from receiving data of the given type.
     * 
     * @param type
     * @param listener
     */
    public <T> void unsubscribeFrom(Class<T> type, Runnable runnable)
    {
        if (!this.subDispatchers.containsKey(type))
        {
            return;
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        dispatcher.unsubscribe(runnable);
    }

    /**
     * Dispatches the given data to all subscribers of that specific data type.
     * 
     * @param data
     */
    public <T> void dispatch(T data)
    {
        if (!this.subDispatchers.containsKey(data.getClass()))
        {
            return;
        }
        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(data.getClass());
        dispatcher.dispatch(data);
    }

    /**
     * Gets all subscribers of the given data type.
     * 
     * @param type
     * @return
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
}