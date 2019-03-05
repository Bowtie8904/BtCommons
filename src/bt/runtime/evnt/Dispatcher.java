package bt.runtime.evnt;

import java.util.HashMap;
import java.util.Map;

/**
 * A basic data dispatcher for generic dispatching of i.e. events to listeners.
 * 
 * @author &#8904
 *
 */
public class Dispatcher
{
    private Map<Class, SubDispatcher> subDispatchers;

    public Dispatcher()
    {
        this.subDispatchers = new HashMap<>();
    }

    /**
     * Subscribes the given listener implementation to the given data type.
     * 
     * <p>
     * If the given data type is {@link #dispatch}ed by this instance, the {@link Listener#receive} method of all
     * listeners that are subscribed to the given type is called and the dispatched data is passed.
     * </p>
     * 
     * @param type
     * @param listener
     */
    public <T> void subscribeTo(Class<T> type, Listener<T> listener)
    {
        if (!this.subDispatchers.containsKey(type))
        {
            this.subDispatchers.put(type, new SubDispatcher<T>());
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        dispatcher.subscribe(listener);
    }

    /**
     * Unsubscribes the given listener from receiving data of the given type.
     * 
     * @param type
     * @param listener
     */
    public <T> void unsubscribeFrom(Class<T> type, Listener<T> listener)
    {
        if (!this.subDispatchers.containsKey(type))
        {
            return;
        }

        var dispatcher = (SubDispatcher<T>)this.subDispatchers.get(type);
        dispatcher.unsubscribe(listener);
    }

    /**
     * Dispatches the given data to all listeners that are subscribed to that specific data type.
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
}