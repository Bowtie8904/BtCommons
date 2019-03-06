package bt.runtime.evnt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author &#8904
 * @param <T>
 *
 */
public class SubDispatcher<T>
{
    private List<Consumer<T>> consumers;
    private List<Runnable> runnables;

    protected SubDispatcher()
    {
        this.consumers = new ArrayList<>();
        this.runnables = new ArrayList<>();
    }

    protected void subscribe(Consumer<T> consumer)
    {
        this.consumers.add(consumer);
    }

    protected void unsubscribe(Consumer<T> consumer)
    {
        this.consumers.remove(consumer);
    }

    protected void subscribe(Runnable runnable)
    {
        this.runnables.add(runnable);
    }

    protected void unsubscribe(Runnable runnable)
    {
        this.runnables.remove(runnable);
    }

    protected void dispatch(T data)
    {
        for (var consumer : this.consumers)
        {
            consumer.accept(data);
        }

        for (var runnable : this.runnables)
        {
            runnable.run();
        }
    }

    public List<Consumer<T>> getSubscribers()
    {
        var list = new ArrayList<Consumer<T>>();
        list.addAll(this.consumers);
        this.runnables.forEach(r -> list.add((d) -> r.run()));

        return list;
    }
}