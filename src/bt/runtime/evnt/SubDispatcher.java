package bt.runtime.evnt;

import java.util.ArrayList;
import java.util.List;

/**
 * @author &#8904
 * @param <T>
 *
 */
public class SubDispatcher<T>
{
    private List<Listener<T>> subscribers;

    protected SubDispatcher()
    {
        this.subscribers = new ArrayList<>();
    }

    protected void subscribe(Listener<T> listener)
    {
        this.subscribers.add(listener);
    }

    protected void unsubscribe(Listener<T> listener)
    {
        this.subscribers.remove(listener);
    }

    protected void dispatch(T data)
    {
        for (var listener : this.subscribers)
        {
            listener.receive(data);
        }
    }
}