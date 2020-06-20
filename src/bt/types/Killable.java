package bt.types;

import java.io.Closeable;

/**
 * Basically a {@link Closeable} clone to be used with {@link InstanceKiller} without closable warnings.
 * 
 * @author &#8904
 */
public interface Killable
{
    /**
     * Terminates all open resources of this instance.
     */
    public void kill();
}