package bt.runtime;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import bt.utils.log.Logger;

/**
 * Class to manage the closing of global resources such as databases and global loggers.
 * 
 * <p>
 * All registered {@link Killable}s will be closed upon Application termination. For a successful kill operation the
 * application should be terminated by calling {@link System#exit(int)} as this class simply uses a shutdown hook.
 * </p>
 * 
 * @author &#8904
 */
public final class InstanceKiller
{
    private static List<Entry<Killable, Integer>> killables = new CopyOnWriteArrayList<>();

    static
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            kill();
        }));
    }

    /**
     * Calls the kill() method of every registered Killable.
     */
    private static void kill()
    {
        Thread.currentThread().setName("INSTANCE_KILLER");
        if (killables.size() > 0)
        {
            Logger.global().print("Killing " + killables.size() + " instances.");
            killables.sort(Comparator.comparing(Entry::getValue, Comparator.reverseOrder()));

            for (Entry<Killable, Integer> killable : killables)
            {
                killable.getKey().kill();
            }
        }
    }

    /**
     * Registers the given Killable to be killed upon application termination. The killable will only be added if it is
     * not already registered.
     * 
     * <p>
     * All Killables added via this method will be killed last, although there is no guarantee that instances which were
     * added will be killed in the same order. The priority attached to the given instance will be
     * {@link Integer#MIN_VALUE} + 1.
     * </p>
     * 
     * <p>
     * The application has to either be closed by calling {@link System#exit(int)} or by terminating every non-daemon
     * thread. Any other way of application termination will cause this class to not kill instances properly.
     * </p>
     * 
     * @param killable
     *            The instance that should be killed on application exit.
     */
    public static synchronized void killOnShutdown(Killable killable)
    {
        killOnShutdown(killable, Integer.MIN_VALUE + 1);
    }

    /**
     * Registers the given Killable to be killed upon application termination. The higher the priority number, the
     * earlier the instance will be killed. The killable will only be added if it is not already registered, even if the
     * priority is different. If you want to update the priority on an already registered killable, you should
     * {@link #unregister(Killable) unregister} and re-add it via this method.
     * 
     * <p>
     * There is no guarantee that instances with the same priority which were added via this method will be killed in
     * the order of registration.
     * </p>
     * 
     * <p>
     * The application has to either be closed by calling {@link System#exit(int)} or by terminating every non-daemon
     * thread. Any other way of application termination will cause this class to not kill instances properly.
     * </p>
     * 
     * @param killable
     *            The instance that should be killed on application exit.
     * @param priority
     *            An arbitrary number which determines the order of termination. The higher the priority, the earlier
     *            the instance will be killed.
     */
    public static synchronized void killOnShutdown(Killable killable, int priority)
    {
        if (!killables.stream().anyMatch(k -> k.getKey().equals(killable)))
        {
            killables.add(new SimpleEntry<Killable, Integer>(killable, priority));
        }
    }

    /**
     * Unregisteres the given killable. Its {@link Killable#kill() kill} method will not be called by this instance
     * killer.
     * 
     * @param killable
     *            The killable to unregister.
     */
    public static synchronized void unregister(Killable killable)
    {
        killables.removeIf(k -> k.getKey().equals(killable));
    }
}