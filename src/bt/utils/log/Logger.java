package bt.utils.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.StackWalker.Option;
import java.lang.invoke.MethodType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import bt.runtime.InstanceKiller;
import bt.runtime.Killable;
import bt.utils.thread.Threads;

/**
 * A logging class which prints to either a given file or to the default log file which is defined by
 * {@link #DEFAULT_LOG_PATH}.
 *
 * <p>
 * All logging from instances of this class can be stopped by calling {@link #setLoggingEnabled(boolean)} with false.
 * </p>
 *
 * @author &#8904
 */
public class Logger implements Killable
{
    /**
     * Indicates whether any logging should be done. If this is set to false, no log files will be created and calls to
     * print have no effect.
     */
    private static boolean loggingEnabled = true;
    private static Logger globalLogger;

    /**
     * The path to the default log file.
     * <p>
     * 'logs/log.txt'
     * </p>
     */
    private static final String DEFAULT_LOG_PATH = "logs/default_logfile.log";

    /** A list which contains all currently active instances of this class. */
    private static ArrayList<Logger> activeLoggers = new ArrayList<>();

    /**
     * A queue which holds strings that are supposed to be logged. This is used as a buffer to bundle write operations.
     */
    private ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(500);

    private ScheduledFuture future;

    /** A list containing all registered sources. */
    private ArrayList<LogSource> logSources = new ArrayList<>();

    /** The {@link PrintWriter} object which is used to log to the desired file. */
    private PrintWriter writer;

    /** The file this Logger instance is writing to. */
    private File logFile;

    /**
     * Indicates wether this Logger instance should also send its output to {@link System#out}. True by default.
     */
    private boolean logToSystemOut = true;

    /** The timezone of this logger. */
    private TimeZone timeZone;

    /**
     * A prefix which will be put in front of the message.
     * <p>
     * '[DEBUG] Debugging message.'
     * </p>
     */
    private String prefix = "";

    /** The date formatter for the logging timestamps. */
    private DateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm:ss.SSS");

    /** Indicates whether this instance is already started and actively running. */
    private boolean isStarted = false;

    /** The interval at which the logger will print to its file. */
    private long logInterval = 30000;

    /** Contains Strings which are used to filter out certain lines. */
    private List<String> filterTexts = new ArrayList<>();

    /** Indicates whether the logger should print information about the calling method. */
    private boolean printCaller = true;

    /**
     * Indicates the position of method in the stack that should be printed as caller method.
     * <p>
     * By default this has the value 2 and will use the method that called print() as the caller.
     * </p>
     */
    private int callerStackIndex = 2;

    /**
     * Indicates whether the Logger should print line instantly or only output lines at the set {@link #logInterval}.
     */
    private boolean printInstant = true;

    /** Indicates whether this logger will actually log when its print methods are called. */
    private boolean enabled = true;

    /**
     * An instance of the {@link Logger} class that is meant for general logging. By default it will print output to the
     * {@link #DEFAULT_LOG_PATH}. This behavior can however be customized by setting an instance with a different
     * configuration via {@link #setGlobal(Logger)}.
     *
     * @return The global logger instance.
     */
    public static Logger global()
    {
        if (globalLogger == null)
        {
            globalLogger = new Logger();
            globalLogger.registerSource(globalLogger,
                                        "GLOBAL_LOGGER");
        }
        return globalLogger;
    }

    /**
     * Sets the global Logger instance that will be used for general logging.
     *
     * @param logger
     */
    public static void setGlobal(Logger logger)
    {
        globalLogger = logger;
    }

    /**
     * If this is set to false, no log files will be created and calls to print have no effect.
     *
     * <p>
     * Instances that are created while this is set to false will not be able to do any logging even after this method
     * is called to re-enable logging.
     * </p>
     */
    public static void setLoggingEnabled(boolean enabled)
    {
        loggingEnabled = enabled;
    }

    /**
     * Creates a {@link Logger} instance which uses the default timezone of the system and prints to the default log
     * file which is defined by {@link #DEFAULT_LOG_PATH}.
     *
     * <p>
     * This logger will be added to the {@link InstanceKiller} via {@link InstanceKiller#killOnShutdown(Killable, int)}
     * with a priority of {@link Integer#MIN_VALUE}.
     * </p>
     */
    public Logger()
    {
        this(DEFAULT_LOG_PATH);
    }

    /**
     * Creates a {@link Logger} instance which uses the default timezone of the system and prints to the file with the
     * given path. If the file does not exist it will be created.
     *
     * <p>
     * This logger will be added to the {@link InstanceKiller} via {@link InstanceKiller#killOnShutdown(Killable, int)}
     * with a priority of {@link Integer#MIN_VALUE}.
     * </p>
     *
     * @param logPath
     *            The path to the desired log file.
     */
    public Logger(String logPath)
    {
        this(new File(logPath));
    }

    /**
     * Creates a {@link Logger} instance which uses the default timezone of the system and prints to the given file. If
     * the file does not exist it will be created.
     *
     * <p>
     * This logger will be added to the {@link InstanceKiller} via {@link InstanceKiller#killOnShutdown(Killable, int)}
     * with a priority of {@link Integer#MIN_VALUE}.
     * </p>
     *
     * @param logFile
     *            The file to which this instance should print.
     */
    public Logger(File logFile)
    {
        setLoggerFile(logFile);
        activeLoggers.add(this);
        InstanceKiller.killOnShutdown(this,
                                      Integer.MIN_VALUE);
    }

    /**
     * Creates a {@link Logger} instance which prints to the default log file which is defined by
     * {@link #DEFAULT_LOG_PATH}. This constructor will set the given timezone as default for the Java VM. Note that
     * this is affecting all {@link Logger} instances.
     *
     * <p>
     * This logger will be added to the {@link InstanceKiller} via {@link InstanceKiller#killOnShutdown(Killable, int)}
     * with a priority of {@link Integer#MIN_VALUE}.
     * </p>
     *
     * @param timeZone
     *            The timezone which should be set as default for the Java VM.
     */
    public Logger(TimeZone timeZone)
    {
        this(DEFAULT_LOG_PATH,
             timeZone);
    }

    /**
     * Creates a {@link Logger} instance which prints to the file with the given path. If the file does not exist it
     * will be created. This constructor will set the given timezone as default for the Java VM. Note that this is
     * affecting all {@link Logger} instances.
     *
     * <p>
     * This logger will be added to the {@link InstanceKiller} via {@link InstanceKiller#killOnShutdown(Killable, int)}
     * with a priority of {@link Integer#MIN_VALUE}.
     * </p>
     *
     * @param logPath
     *            The path to the desired log file.
     * @param timeZone
     *            The timezone which should be set as default for the Java VM.
     */
    public Logger(String logPath, TimeZone timeZone)
    {
        this(new File(logPath),
             timeZone);
    }

    /**
     * Creates a {@link Logger} instance which prints to the given file. If the file does not exist it will be created.
     * This constructor will set the given timezone as default for the Java VM. Note that this is affecting all
     * {@link Logger} instances.
     *
     * <p>
     * This logger will be added to the {@link InstanceKiller} via {@link InstanceKiller#killOnShutdown(Killable, int)}
     * with a priority of {@link Integer#MIN_VALUE}.
     * </p>
     *
     * @param logFile
     *            The file to which this instance should print.
     * @param timeZone
     *            The timezone which should be set as default for the Java VM.
     */
    public Logger(File logFile, TimeZone timeZone)
    {
        setLoggerFile(logFile);
        this.timeZone = timeZone;
        activeLoggers.add(this);
        InstanceKiller.killOnShutdown(this,
                                      Integer.MIN_VALUE);
    }

    /**
     * Creates a {@link Logger} instance which prints to the file with the given path. If the file does not exist it
     * will be created. This constructor will set the given timezone as default for the Java VM. Note that this is
     * affecting all {@link Logger} instances.
     *
     * <p>
     * This logger will be added to the {@link InstanceKiller} via {@link InstanceKiller#killOnShutdown(Killable, int)}
     * with a priority of {@link Integer#MIN_VALUE}.
     * </p>
     *
     * @param logPath
     *            The path to the desired log file.
     * @param timeZone
     *            The id of the timezone which should be set as default for the Java VM. If the id is not recognized the
     *            timezone will be set to GMT.
     */
    public Logger(String logPath, String timeZone)
    {
        this(new File(logPath),
             timeZone);
    }

    /**
     * Creates a {@link Logger} instance which prints to the given file. If the file does not exist it will be created.
     * This constructor will set the given timezone as default for the Java VM. Note that this is affecting all
     * {@link Logger} instances.
     *
     * <p>
     * This logger will be added to the {@link InstanceKiller} via {@link InstanceKiller#killOnShutdown(Killable, int)}
     * with a priority of {@link Integer#MIN_VALUE}.
     * </p>
     *
     * @param logFile
     *            The file to which this instance should print.
     * @param timeZone
     *            The id of the timezone which should be set as default for the Java VM. If the id is not recognized the
     *            timezone will be set to GMT.
     */
    public Logger(File logFile, String timeZone)
    {
        setLoggerFile(logFile);
        this.timeZone = TimeZone.getTimeZone(timeZone);
        activeLoggers.add(this);
        InstanceKiller.killOnShutdown(this,
                                      Integer.MIN_VALUE);
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    /**
     * Closes this instances {@link #writer} and removes it from the {@link #activeLoggers} list.
     *
     * <p>
     * If this instance is set to log at a specified interval {@link #logQueue()} is called before closing resources.
     * </p>
     */
    @Override
    public void kill()
    {
        if (!this.printInstant)
        {
            this.printInstant = true;
            logQueue();
        }

        print(this,
              "Closing logger.");
        if (this.writer != null)
        {
            this.writer.close();
        }
        activeLoggers.remove(this);
        if (this.future != null)
        {
            this.future.cancel(true);
        }
        this.isStarted = false;
    }

    /**
     * Calls the {@link #kill()} method of all active loggers.
     */
    public static void killAll()
    {
        for (Logger logger : activeLoggers)
        {
            logger.kill();
        }
        activeLoggers.clear();
    }

    /**
     * Sets the {@link #logFile}.
     *
     * @param path
     *            The path to the desired log file.
     */
    public void setLoggerFile(String path)
    {
        setLoggerFile(new File(path));
    }

    /**
     * Sets the {@link #logFile}.
     *
     * <p>
     * This method has no effect if {@link #setLoggingEnabled(boolean)} was set to false.
     * </p>
     *
     * @param logFile
     *            The desired log file.
     */
    public void setLoggerFile(File logFile)
    {
        if (loggingEnabled)
        {
            try
            {
                this.logFile = logFile;
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
                try
                {
                    if (this.writer != null)
                    {
                        this.writer.close();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                this.writer = new PrintWriter(new BufferedWriter(new FileWriter(this.logFile,
                                                                                true)),
                                              true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the current {@link #logFile}.
     *
     * @return The current {@link #logFile}.
     */
    public File getLoggerFile()
    {
        return this.logFile;
    }

    /**
     * Sets wether this instance should also print to {@link System#out}.
     *
     * @param b
     *            True if this insatnce should also print to {@link System#out}.
     */
    public void setLogToSystemOut(boolean b)
    {
        this.logToSystemOut = b;
    }

    /**
     * Indicates wether this instance is also printing to {@link System#out}.
     *
     * @return True if this insatnce is also printing to {@link System#out}.
     */
    public boolean isLogToSystemOut()
    {
        return this.logToSystemOut;
    }

    /**
     * Sets a prefix which is put in front of the log message.
     *
     * @param prefix
     *            The prefix.
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    /**
     * Sets the timezone of this {@link Logger} instance to the timezone with the given id. If the id is not recognized
     * the timezone will be set to GMT.
     *
     * @param id
     *            The id of the desired timezone.
     */
    public void setTimeZone(String id)
    {
        this.timeZone = TimeZone.getTimeZone(id);
    }

    /**
     * Sets the timezone of this {@link Logger} insatnce to the given one. Note that this is affecting all
     * {@link Logger} instances.
     *
     * @param zone
     *            The desired timezone.
     */
    public void setTimeZone(TimeZone timeZone)
    {
        this.timeZone = timeZone;
    }

    /**
     * Gets the current timezone of this {@link Logger} insatnce .
     *
     * @return The currently set default timezone of this instance.
     */
    public TimeZone getTimeZone()
    {
        return this.timeZone;
    }

    /**
     * Gets this instances {@link #writer}.
     *
     * @return This instances {@link #writer}.
     */
    public Writer getPrintWriter()
    {
        return this.writer;
    }

    /**
     * Formats a date String with the set {@link TimeZone}.
     *
     * @return the date String.
     */
    public String getDateString()
    {
        Calendar calendar = new GregorianCalendar();

        this.formatter.setCalendar(calendar);

        if (this.timeZone != null)
        {
            this.formatter.setTimeZone(this.timeZone);
        }

        return "[" + this.formatter.format(calendar.getTime()) + "]";
    }

    /**
     * Registers the given source so its name can be displayed when logging.
     *
     * @param source
     *            The LogSource object which represents the logging object.
     * @return The name of the LogSource.
     */
    public String registerSource(LogSource source)
    {
        this.logSources.add(source);
        return source.getName();
    }

    /**
     * Registers the given source so its name can be displayed when logging.
     *
     * @param source
     *            The instance which will use this logger.
     * @param name
     *            The name which will be displayed while logging.
     * @return The name of the LogSource.
     */
    public String registerSource(Object source, String name)
    {
        return registerSource(new LogSource(source,
                                            name));
    }

    /**
     * Registers the given source and finds a unique name to be displayed while logging from inside that source.
     *
     * @param source
     *            The instance which will use this logger.
     * @return The name of the LogSource.
     */
    public String registerSource(Object source)
    {
        int nameExt = 1;
        boolean exists = true;

        while (exists)
        {
            exists = false;
            for (LogSource logSource : this.logSources)
            {
                if (logSource.getName().equals(source.getClass().getName() + "-" + nameExt))
                {
                    nameExt ++ ;
                    exists = true;
                    break;
                }
            }
        }
        return registerSource(source,
                              source.getClass().getName() + "-" + nameExt);
    }

    /**
     * Returns the name of the registered source or null if the source is not registered.
     *
     * @param source
     *            The instance whichs name should be found.
     * @return The name with which the source was registered or null if it was not registere.
     */
    public String getSourceName(Object source)
    {
        if (source == null)
        {
            return null;
        }
        for (LogSource logSource : this.logSources)
        {
            if (logSource.getSource() == source)
            {
                return logSource.getName();
            }
        }
        return null;
    }

    /**
     * Starts the timer which will log all buffered lines to the dedicated file.
     *
     * The default interval is 30 seconds.
     */
    public void start()
    {
        this.printInstant = false;

        if (!this.isStarted)
        {
            this.isStarted = true;
            this.future = Threads.get().scheduleWithFixedDelayDaemon(new Runnable()
            {
                @Override
                public void run()
                {
                    logQueue();
                }
            },
                                                                     this.logInterval,
                                                                     this.logInterval,
                                                                     TimeUnit.MILLISECONDS,
                                                                     "LOGGER_QUEUE");
        }
    }

    /**
     * Logs all buffered lines to the set file.
     */
    private synchronized void logQueue()
    {
        if (this.enabled && loggingEnabled)
        {
            ArrayList<String> copy = new ArrayList<>(this.queue);
            this.queue.clear();
            for (String line : copy)
            {
                this.writer.println(line);
            }
            this.writer.flush();
        }
    }

    /**
     * Checks if the given String contains any of the Strings from the filterTexts list.
     *
     * @param line
     *            The line to check.
     * @return true if the line contains any, false otherwise.
     */
    private boolean containsFilter(String line)
    {
        for (String filter : this.filterTexts)
        {
            if (line.toLowerCase().contains(filter.toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the given text as a filter.
     *
     * Any lines from the logfile that contains a filter will not be sent to the channel.
     *
     * @param filter
     *            The filter text to be added.
     */
    public void addFilterText(String filter)
    {
        this.filterTexts.add(filter);
    }

    /**
     * Sets the interval at which the logger will print to its file.
     *
     * Calling this after calling {@link #start()} has no effect.
     *
     * @param interval
     *            The interval in miliseconds.
     */
    public void setLogInterval(long interval)
    {
        this.logInterval = interval;
    }

    /**
     * Sets whether the logger will log the name of the method it was called from.
     *
     * @param printCaller
     *            true to log additional information about the class, method and line number of the print calls.
     */
    public void setPrintCaller(boolean printCaller)
    {
        this.printCaller = printCaller;
    }

    /**
     * Sets the index of the method in the call stack that should be used to print caller information.
     *
     * <p>
     * The default value is 2, so it will use the method that called print() as a caller.
     * </p>
     *
     * @param index
     */
    public void setCallerStackIndex(int index)
    {
        if (index < 0)
        {
            return;
        }
        this.callerStackIndex = index;
    }

    private String getCallerString(int stackIndex)
    {
        var stack = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
                               .walk(s -> s.skip(stackIndex)
                                           .findFirst())
                               .get();

        String className = stack.getClassName();
        String methodName = stack.getMethodName();
        MethodType type = stack.getMethodType();

        var str = new StringBuilder();
        str.append("[");
        str.append(className);
        str.append(".");
        str.append(methodName);
        str.append("(");

        for (Class<?> param : type.parameterArray())
        {
            str.append(param.getSimpleName());
            str.append(", ");
        }

        String ret = str.toString();
        if (type.parameterCount() > 0)
        {
            ret = ret.substring(0,
                                ret.length() - 2);
        }
        str.setLength(0);
        str.append(ret);
        str.append(") : ");
        str.append(stack.getLineNumber());
        str.append("]");

        String result = str.toString();

        if (result.contains(getClass().getName()))
        {
            result = getCallerString(stackIndex + 2);
        }

        return result;
    }

    private String getCallerString()
    {
        return getCallerString(this.callerStackIndex);
    }

    /**
     * Prints an empty line withhout prefix.
     */
    public void printEmpty()
    {
        if (this.enabled && loggingEnabled)
        {
            if (activeLoggers.contains(this))
            {
                if (this.logToSystemOut)
                {
                    System.out.println("");
                }

                if (this.printInstant)
                {
                    this.writer.println();
                }
                else
                {
                    try
                    {
                        this.queue.put("");
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                throw new ClosedLoggerException("Closed loggers can't print.");
            }
        }
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param s
     *            The text that should be logged.
     */
    public void print(Object source, String s)
    {
        if (this.enabled && loggingEnabled)
        {
            if (activeLoggers.contains(this))
            {
                String text;
                String sourceName = getSourceName(source);
                var str = new StringBuilder();
                str.append(getDateString());
                str.append(" ");

                if (this.printCaller)
                {
                    str.append(getCallerString());
                    str.append(" ");
                }

                str.append(this.prefix);

                if (source == null)
                {
                    str.append(s);
                }
                else if (sourceName == null)
                {
                    str.append("[");
                    str.append(source.toString());
                    str.append("] ");
                    str.append(s);
                }
                else
                {
                    str.append("[");
                    str.append(sourceName);
                    str.append("] ");
                    str.append(s);
                }

                text = str.toString();

                if (!containsFilter(text))
                {
                    if (this.logToSystemOut)
                    {
                        System.out.println(text);
                    }
                    if (this.printInstant)
                    {
                        this.writer.println(text);
                    }
                    else
                    {
                        try
                        {
                            this.queue.put(text);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else
            {
                throw new ClosedLoggerException("Closed loggers can't print.");
            }
        }
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param b
     */
    public void print(Object source, boolean b)
    {
        print(source,
              Boolean.toString(b));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param i
     */
    public void print(Object source, int i)
    {
        print(source,
              Integer.toString(i));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param s
     */
    public void print(Object source, short s)
    {
        print(source,
              Short.toString(s));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param l
     */
    public void print(Object source, long l)
    {
        print(source,
              Long.toString(l));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param d
     */
    public void print(Object source, double d)
    {
        print(source,
              Double.toString(d));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param f
     */
    public void print(Object source, float f)
    {
        print(source,
              Float.toString(f));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param c
     */
    public void print(Object source, char c)
    {
        print(source,
              Character.toString(c));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param b
     */
    public void print(Object source, byte b)
    {
        print(source,
              Byte.toString(b));
    }

    /**
     * Prints the String returned by the given object's {@link Object#toString()} method with the current date to the
     * {@link #logFile} and, if {@link #logToSystemOut} is true, to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param o
     */
    public void print(Object source, Object o)
    {
        print(source,
              o == null ? "null" : o.toString());
    }

    /**
     * Prints the message of the given throwable with the current date and the stacktrace to the {@link #logFile} and,
     * if {@link #logToSystemOut} is true, to {@link System#out}.
     *
     * @param source
     *            The source of this logging request.
     * @param t
     */
    public void print(Object source, Throwable t)
    {
        if (this.enabled && loggingEnabled)
        {
            if (activeLoggers.contains(this))
            {
                String text;
                String sourceName = getSourceName(source);
                var str = new StringBuilder();
                str.append(getDateString());
                str.append(" ");

                if (this.printCaller)
                {
                    str.append(getCallerString());
                    str.append(" ");
                }

                str.append(this.prefix);

                if (source == null)
                {
                    str.append(" ERROR");
                }
                else if (sourceName == null)
                {
                    str.append("[");
                    str.append(source.toString());
                    str.append("] ");
                    str.append(" ERROR");
                }
                else
                {
                    str.append("[");
                    str.append(sourceName);
                    str.append("] ");
                    str.append(" ERROR");
                }

                text = str.toString();

                if (this.logToSystemOut)
                {
                    System.out.println(text);
                    t.printStackTrace();
                }
                try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw))
                {
                    t.printStackTrace(pw);
                    String trace = sw.toString();
                    if (!containsFilter(trace))
                    {
                        if (this.printInstant)
                        {
                            this.writer.println(text + System.lineSeparator() + trace);
                        }
                        else
                        {
                            try
                            {
                                this.queue.put(text + System.lineSeparator() + trace);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                throw new ClosedLoggerException("Closed loggers can't print.");
            }
        }
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param s
     *            The text that should be logged.
     */
    public void print(String s)
    {
        if (this.enabled && loggingEnabled)
        {
            if (activeLoggers.contains(this))
            {
                if (!containsFilter(s))
                {
                    var str = new StringBuilder();
                    str.append(getDateString());
                    str.append(" ");

                    if (this.printCaller)
                    {
                        str.append(getCallerString());
                        str.append(" ");
                    }

                    str.append(this.prefix);
                    str.append(" ");
                    str.append(s);

                    String text = str.toString();

                    if (this.logToSystemOut)
                    {
                        System.out.println(text);
                    }

                    if (this.printInstant)
                    {
                        this.writer.println(text);
                    }
                    else
                    {
                        try
                        {
                            this.queue.put(text);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else
            {
                throw new ClosedLoggerException("Closed loggers can't print.");
            }
        }
    }

    public void printf(String s, Object... args)
    {
        print(String.format(s,
                            args));
    }

    public void printfSrc(Object source, String s, Object... args)
    {
        print(source,
              String.format(s,
                            args));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param b
     */
    public void print(boolean b)
    {
        print(Boolean.toString(b));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param i
     */
    public void print(int i)
    {
        print(Integer.toString(i));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param s
     */
    public void print(short s)
    {
        print(Short.toString(s));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param l
     */
    public void print(long l)
    {
        print(Long.toString(l));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param d
     */
    public void print(double d)
    {
        print(Double.toString(d));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param f
     */
    public void print(float f)
    {
        print(Float.toString(f));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param c
     */
    public void print(char c)
    {
        print(Character.toString(c));
    }

    /**
     * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true,
     * to {@link System#out}.
     *
     * @param b
     */
    public void print(byte b)
    {
        print(Byte.toString(b));
    }

    /**
     * Prints the String returned by the given object's {@link Object#toString()} method with the current date to the
     * {@link #logFile} and, if {@link #logToSystemOut} is true, to {@link System#out}.
     *
     * @param o
     */
    public void print(Object o)
    {
        print(o == null ? "null" : o.toString());
    }

    /**
     * Prints the message of the given throwable with the current date and the stacktrace to the {@link #logFile} and,
     * if {@link #logToSystemOut} is true, to {@link System#out}.
     *
     * @param t
     */
    public void print(Throwable t)
    {
        if (this.enabled && loggingEnabled)
        {
            if (activeLoggers.contains(this))
            {
                var str = new StringBuilder();
                str.append(getDateString());
                str.append(" ");

                if (this.printCaller)
                {
                    str.append(getCallerString());
                    str.append(" ");
                }

                str.append(this.prefix);
                str.append(" ERROR");

                String text = str.toString();

                if (this.logToSystemOut)
                {
                    System.out.println(text);
                    t.printStackTrace();
                }
                try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw))
                {
                    t.printStackTrace(pw);
                    String trace = sw.toString();
                    if (!containsFilter(trace))
                    {
                        if (this.printInstant)
                        {
                            this.writer.println(text + System.lineSeparator() + trace);
                        }
                        else
                        {
                            try
                            {
                                this.queue.put(text + System.lineSeparator() + trace);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                throw new ClosedLoggerException("Closed loggers can't print.");
            }
        }
    }
}