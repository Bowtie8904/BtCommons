package bt.utils.thread;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import bt.runtime.InstanceKiller;
import bt.runtime.Killable;
import bt.utils.log.Logger;

/**
 * Holds multiple threadpools with different purposes.
 * 
 * @author &#8904
 */
public class Threads implements Killable
{
    private static Threads instance;

    private final ScheduledThreadPoolExecutor schedulerPool;
    private final ExecutorService cachedPool;
    private final ScheduledThreadPoolExecutor schedulerPoolDaemon;
    private final ExecutorService cachedPoolDaemon;
    private volatile List<ExecutorService> additionalPools;

    /**
     * Creates a new instacne and initializes all pools.
     */
    protected Threads()
    {
        this.schedulerPool = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(10);
        this.schedulerPool.setRemoveOnCancelPolicy(true);
        this.cachedPool = Executors.newCachedThreadPool();

        // factory purely to create ddaemon threads
        ThreadFactory daemonFactory = new ThreadFactory()
        {
            @Override
            public Thread newThread(Runnable r)
            {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        };

        this.schedulerPoolDaemon = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(10, daemonFactory);
        this.schedulerPoolDaemon.setRemoveOnCancelPolicy(true);
        this.cachedPoolDaemon = Executors.newCachedThreadPool(daemonFactory);

        this.additionalPools = new CopyOnWriteArrayList<>();
    }

    /**
     * Gets the singleton instance.
     * 
     * <p>
     * If no instance has been initialized before this method will create a new one.
     * </p>
     * 
     * @return The instance.
     */
    public static Threads get()
    {
        if (instance == null)
        {
            instance = new Threads();
            Logger.global().registerSource(instance, "THREADS");
            InstanceKiller.closeOnShutdown(instance, Integer.MIN_VALUE + 2);
        }
        return instance;
    }

    /**
     * Executes the given runnable in a new non-daemon thread.
     * 
     * @param task
     *            The runnable to execute.
     */
    public void execute(Runnable task)
    {
        new Thread(task).start();
    }

    /**
     * Executes the given runnable in a new non-daemon thread.
     * 
     * @param threadName
     *            The name of the created thread.
     * @param task
     *            The runnable to execute.
     */
    public void execute(Runnable task, String threadName)
    {
        new Thread(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        }).start();
    }

    /**
     * Executes the given runnable in a new daemon thread.
     * 
     * @param task
     *            The runnable to execute.
     */
    public void executeDaemon(Runnable task)
    {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Executes the given runnable in a new daemon thread.
     * 
     * @param threadName
     *            The name of the created thread.
     * @param task
     *            The runnable to execute.
     */
    public void executeDaemon(Runnable task, String threadName)
    {
        Thread thread = new Thread(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Executes the given runnable in a non-daemon thread from the cashed thread pool.
     * 
     * @param task
     *            The runnable to execute.
     */
    public void executeCached(Runnable task)
    {
        this.cachedPool.execute(task);
    }

    /**
     * Executes the given runnable in a non-daemon thread from the cashed thread pool.
     * 
     * @param threadName
     *            The name of the used thread.
     * @param task
     *            The runnable to execute.
     */
    public void executeCached(Runnable task, String threadName)
    {
        this.cachedPool.execute(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        });
    }

    /**
     * Executes the given runnable in a daemon thread from the cashed thread pool.
     * 
     * @param task
     *            The runnable to execute.
     */
    public void executeCachedDaemon(Runnable task)
    {
        this.cachedPoolDaemon.execute(task);
    }

    /**
     * Executes the given runnable in a daemon thread from the cashed thread pool.
     * 
     * @param threadName
     *            The name of the used thread.
     * @param task
     *            The runnable to execute.
     */
    public void executeCachedDaemon(Runnable task, String threadName)
    {
        this.cachedPoolDaemon.execute(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        });
    }

    /**
     * Executes the given runnable after the set delay in a pooled non-daemon thread.
     * 
     * @param task
     *            The runnable to execute.
     * @param delay
     *            The delay after which the task will be executed.
     * @param unit
     *            The time unit of the delay.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit)
    {
        return this.schedulerPool.schedule(() ->
        {
            task.run();
        }, delay, unit);
    }

    /**
     * Executes the given runnable after the set delay in a pooled non-daemon thread.
     * 
     * @param task
     *            The runnable to execute.
     * @param delay
     *            The delay after which the task will be executed.
     * @param unit
     *            The time unit of the delay.
     * @param threadName
     *            The name of the used thread.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit, String threadName)
    {
        return this.schedulerPool.schedule(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        }, delay, unit);
    }

    /**
     * Executes the given runnable after the set delay in a pooled daemon thread.
     * 
     * @param task
     *            The runnable to execute.
     * @param delay
     *            The delay after which the task will be executed.
     * @param unit
     *            The time unit of the delay.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleDaemon(Runnable task, long delay, TimeUnit unit)
    {
        return this.schedulerPoolDaemon.schedule(() ->
        {
            task.run();
        }, delay, unit);
    }

    /**
     * Executes the given runnable after the set delay in a pooled daemon thread.
     * 
     * @param task
     *            The runnable to execute.
     * @param delay
     *            The delay after which the task will be executed.
     * @param unit
     *            The time unit of the delay.
     * @param threadName
     *            The name of the used thread.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleDaemon(Runnable task, long delay, TimeUnit unit, String threadName)
    {
        return this.schedulerPoolDaemon.schedule(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        }, delay, unit);
    }

    /**
     * Creates and executes a periodic action that becomes enabled firstafter the given initial delay, and subsequently
     * with the given period; that is executions will commence after initialDelay then initialDelay+period, then
     * initialDelay + 2 * period, and so on.If any execution of the taskencounters an exception, subsequent executions
     * are suppressed.Otherwise, the task will only terminate via cancellation ortermination of the executor. If any
     * execution of this tasktakes longer than its period, then subsequent executionsmay start late, but will not
     * concurrently execute.
     * 
     * @param task
     *            The runnable to execute.
     * @param initialDelay
     *            The delay before the first execution.
     * @param period
     *            The period between executions.
     * @param unit
     *            The time unit of the delays.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit)
    {
        return this.schedulerPool.scheduleAtFixedRate(() ->
        {
            task.run();
        }, initialDelay, period, unit);
    }

    /**
     * Creates and executes a periodic action that becomes enabled firstafter the given initial delay, and subsequently
     * with the given period; that is executions will commence after initialDelay then initialDelay+period, then
     * initialDelay + 2 * period, and so on.If any execution of the taskencounters an exception, subsequent executions
     * are suppressed.Otherwise, the task will only terminate via cancellation ortermination of the executor. If any
     * execution of this tasktakes longer than its period, then subsequent executionsmay start late, but will not
     * concurrently execute.
     * 
     * @param task
     *            The runnable to execute.
     * @param initialDelay
     *            The delay before the first execution.
     * @param period
     *            The period between executions.
     * @param unit
     *            The time unit of the delays.
     * @param threadName
     *            The name of the used thread.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit,
            String threadName)
    {
        return this.schedulerPool.scheduleAtFixedRate(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        }, initialDelay, period, unit);
    }

    /**
     * Creates and executes a periodic action that becomes enabled firstafter the given initial delay, and subsequently
     * with the given period; that is executions will commence after initialDelay then initialDelay+period, then
     * initialDelay + 2 * period, and so on.If any execution of the taskencounters an exception, subsequent executions
     * are suppressed.Otherwise, the task will only terminate via cancellation ortermination of the executor. If any
     * execution of this tasktakes longer than its period, then subsequent executionsmay start late, but will not
     * concurrently execute.
     * 
     * @param task
     *            The runnable to execute.
     * @param initialDelay
     *            The delay before the first execution.
     * @param period
     *            The period between executions.
     * @param unit
     *            The time unit of the delays.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleAtFixedRateDaemon(Runnable task, long initialDelay, long period, TimeUnit unit)
    {
        return this.schedulerPoolDaemon.scheduleAtFixedRate(() ->
        {
            task.run();
        }, initialDelay, period, unit);
    }

    /**
     * Creates and executes a periodic action that becomes enabled firstafter the given initial delay, and subsequently
     * with the given period; that is executions will commence after initialDelay then initialDelay+period, then
     * initialDelay + 2 * period, and so on.If any execution of the taskencounters an exception, subsequent executions
     * are suppressed.Otherwise, the task will only terminate via cancellation ortermination of the executor. If any
     * execution of this tasktakes longer than its period, then subsequent executionsmay start late, but will not
     * concurrently execute.
     * 
     * @param task
     *            The runnable to execute.
     * @param initialDelay
     *            The delay before the first execution.
     * @param period
     *            The period between executions.
     * @param unit
     *            The time unit of the delays.
     * @param threadName
     *            The name of the used thread.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleAtFixedRateDaemon(Runnable task, long initialDelay, long period, TimeUnit unit,
            String threadName)
    {
        return this.schedulerPoolDaemon.scheduleAtFixedRate(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        }, initialDelay, period, unit);
    }

    /**
     * Creates and executes a periodic action that becomes enabled firstafter the given initial delay, and subsequently
     * with the given delay between the termination of one execution and thecommencement of the next. If any execution
     * of the taskencounters an exception, subsequent executions are suppressed. Otherwise, the task will only terminate
     * via cancellation ortermination of the executor.
     * 
     * @param task
     *            The runnable to execute.
     * @param initialDelay
     *            The delay before the first execution.
     * @param delay
     *            The delay between executions.
     * @param unit
     *            The time unit of the delays.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit)
    {
        return this.schedulerPool.scheduleWithFixedDelay(() ->
        {
            task.run();
        }, initialDelay, delay, unit);
    }

    /**
     * Creates and executes a periodic action that becomes enabled firstafter the given initial delay, and subsequently
     * with the given delay between the termination of one execution and thecommencement of the next. If any execution
     * of the taskencounters an exception, subsequent executions are suppressed. Otherwise, the task will only terminate
     * via cancellation ortermination of the executor.
     * 
     * @param task
     *            The runnable to execute.
     * @param initialDelay
     *            The delay before the first execution.
     * @param delay
     *            The delay between executions.
     * @param unit
     *            The time unit of the delays.
     * @param threadName
     *            The name of the used thread.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit,
            String threadName)
    {
        return this.schedulerPool.scheduleWithFixedDelay(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        }, initialDelay, delay, unit);
    }

    /**
     * Creates and executes a periodic action that becomes enabled firstafter the given initial delay, and subsequently
     * with the given delay between the termination of one execution and thecommencement of the next. If any execution
     * of the taskencounters an exception, subsequent executions are suppressed. Otherwise, the task will only terminate
     * via cancellation ortermination of the executor.
     * 
     * @param task
     *            The runnable to execute.
     * @param initialDelay
     *            The delay before the first execution.
     * @param delay
     *            The delay between executions.
     * @param unit
     *            The time unit of the delays.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleWithFixedDelayDaemon(Runnable task, long initialDelay, long delay, TimeUnit unit)
    {
        return this.schedulerPoolDaemon.scheduleWithFixedDelay(() ->
        {
            task.run();
        }, initialDelay, delay, unit);
    }

    /**
     * Creates and executes a periodic action that becomes enabled firstafter the given initial delay, and subsequently
     * with the given delay between the termination of one execution and thecommencement of the next. If any execution
     * of the taskencounters an exception, subsequent executions are suppressed. Otherwise, the task will only terminate
     * via cancellation ortermination of the executor.
     * 
     * @param task
     *            The runnable to execute.
     * @param initialDelay
     *            The delay before the first execution.
     * @param delay
     *            The delay between executions.
     * @param unit
     *            The time unit of the delays.
     * @param threadName
     *            The name of the used thread.
     * @return A ScheduledFuture representing pending completion ofthe task and whose get() method will return null upon
     *         completion.
     */
    public ScheduledFuture<?> scheduleWithFixedDelayDaemon(Runnable task, long initialDelay, long delay, TimeUnit unit,
            String threadName)
    {
        return this.schedulerPoolDaemon.scheduleWithFixedDelay(() ->
        {
            Thread.currentThread().setName(threadName);
            task.run();
        }, initialDelay, delay, unit);
    }

    /**
     * @see bt.close.Killable#kill()
     */
    @Override
    public void kill()
    {
        Logger.global().print(this, "Shutting down thread pools.");
        Thread.currentThread().setName("THREADS_SHUTDOWN");
        this.schedulerPool.shutdown();
        this.schedulerPool.shutdownNow();
        this.cachedPool.shutdown();
        this.cachedPool.shutdownNow();
        this.schedulerPoolDaemon.shutdown();
        this.schedulerPoolDaemon.shutdownNow();
        this.cachedPoolDaemon.shutdown();
        this.cachedPoolDaemon.shutdownNow();

        for (ExecutorService service : this.additionalPools)
        {
            service.shutdown();
            service.shutdownNow();
        }
    }

    /**
     * Adds another ExecutorService to this instance.
     * 
     * <p>
     * The added service will be shut down when this instances kill() is called.
     * </p>
     * 
     * @param service
     *            The ExecutorService to add.
     */
    public synchronized void add(ExecutorService service)
    {
        this.additionalPools.add(service);
    }
}