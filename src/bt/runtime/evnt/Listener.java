package bt.runtime.evnt;

/**
 * @author &#8904
 *
 */
public interface Listener<T>
{
    public void receive(T data);
}