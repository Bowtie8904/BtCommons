package bt.runtime;

import bt.log.Logger;
import bt.types.Killable;
import bt.utils.Null;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private volatile static boolean isActive;
    private static boolean logActivity = true;

    static
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                                                        {
                                                            InstanceKiller.kill();
                                                        }));
    }

    /**
     * Indicates whether the InstanceKiller is currently looping through all registered Killables to call their kill
     * method.
     *
     * @return true if the InstanceKiller is currently calling kill methods.
     */
    public static boolean isActive()
    {
        return InstanceKiller.isActive;
    }

    /**
     * Indicates whether logs should be written whenever an instance is registered or unregistered.
     *
     * @param log
     */
    public static void logActivity(boolean log)
    {
        InstanceKiller.logActivity = log;
    }

    /**
     * Calls the kill() method of every registered Killable.
     */
    private static void kill()
    {
        Thread.currentThread().setName("INSTANCE_KILLER");
        if (InstanceKiller.killables.size() > 0)
        {
            InstanceKiller.isActive = true;

            System.out.println("Killing " + InstanceKiller.killables.size() + (InstanceKiller.killables.size() > 1 ? " instances." : " instance."));
            InstanceKiller.killables.sort(Comparator.comparing(Entry::getValue,
                                                               Comparator.reverseOrder()));

            for (Entry<Killable, Integer> killable : InstanceKiller.killables)
            {
                Logger.global().setCallerStackIndex(0);
                Null.checkKill(killable.getKey());
                Logger.global().setCallerStackIndex(3);
            }

            InstanceKiller.isActive = false;
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
     * @param killable The instance that should be killed on application exit.
     */
    public static synchronized void killOnShutdown(Killable killable)
    {
        InstanceKiller.killOnShutdown(killable, Integer.MIN_VALUE + 1);
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
     * @param killable The instance that should be killed on application exit.
     * @param priority An arbitrary number which determines the order of termination. The higher the priority, the earlier
     *                 the instance will be killed.
     */
    public static synchronized void killOnShutdown(Killable killable, int priority)
    {
        if (!InstanceKiller.isRegistered(killable))
        {
            InstanceKiller.killables.add(new SimpleEntry<>(killable, priority));

            if (InstanceKiller.logActivity)
            {
                System.out.println("Registered type " + killable.getClass().getName() + " for killing with a priority of " + priority + ".");
            }
        }
    }

    /**
     * Unregisteres the given killable. Its {@link Killable#kill() kill} method will not be called by this instance
     * killer.
     *
     * @param killable The killable to unregister.
     */
    public static synchronized void unregister(Killable killable)
    {
        boolean removed = InstanceKiller.killables.removeIf(k -> k.getKey().equals(killable));

        if (removed && InstanceKiller.logActivity)
        {
            System.out.println("Unregistered type " + killable.getClass().getName() + " from killing.");
        }
    }

    /**
     * Indicates whether the given killable is already registered for termination.
     *
     * @param killable
     * @return true if the killable is already registered.
     */
    public static synchronized boolean isRegistered(Killable killable)
    {
        return InstanceKiller.killables.stream().anyMatch(k -> k.getKey().equals(killable));
    }
}