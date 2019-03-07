package bt.utils.thread.fact;

import java.util.concurrent.ThreadFactory;

/**
 * A simple thread factory implementation to create daemon threads.
 * 
 * @author &#8904
 */
public class DaemonThreadFactory implements ThreadFactory
{
    /**
     * Creates a new daemon thread.
     * 
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    @Override
    public Thread newThread(Runnable r)
    {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}