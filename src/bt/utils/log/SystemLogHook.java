package bt.utils.log;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author &#8904
 *
 */
public class SystemLogHook extends PrintStream
{
    private static final int PRINT_CALLER_STACK_INDEX = 5;
    private static final int NON_FORWARD_CALLER_STACK_INDEX = 6;
    private static final int PRINTLN_CALLER_STACK_INDEX = 7;
    private static final int FORMAT_CALLER_STACK_INDEX = 8;
    private static boolean outInit;
    private static boolean errInit;
    private static SystemLogHook outHook;
    private static SystemLogHook errHook;

    private PrintStream outStream;
    private List<Logger> subscribedLoggers;
    private int currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    private boolean forwardOutput;

    public static SystemLogHook err()
    {
        return errHook;
    }

    public static SystemLogHook out()
    {
        return outHook;
    }

    private static void initErrHook()
    {
        if (!errInit)
        {
            PrintStream errStream = System.err;
            errHook = new SystemLogHook(errStream);
            System.setErr(errHook);
            errInit = true;
        }
    }

    private static void initOutHook()
    {
        if (!outInit)
        {
            PrintStream outStream = System.out;
            outHook = new SystemLogHook(outStream);
            System.setOut(outHook);
            outInit = true;
        }
    }

    private SystemLogHook(PrintStream stream)
    {
        super(stream);
        this.outStream = stream;
        this.subscribedLoggers = new ArrayList<>();
    }

    public void forwardOutput(boolean forward)
    {
        this.forwardOutput = forward;
    }

    public void subscribe(Logger log)
    {
        this.subscribedLoggers.add(log);
    }

    public void unsubscribe(Logger log)
    {
        this.subscribedLoggers.remove(log);
    }

    public static void resetSystemOut()
    {
        if (outHook != null)
        {
            System.setOut(new PrintStream(outHook.out));
        }
    }

    public static void resetSystemErr()
    {
        if (errHook != null)
        {
            System.setErr(new PrintStream(errHook.out));
        }
    }

    public static void hookOut(Logger log)
    {
        initOutHook();
        outHook.subscribe(log);
    }

    public static void hookErr(Logger log)
    {
        initErrHook();
        errHook.subscribe(log);
    }

    public static void unhookOut(Logger log)
    {
        outHook.unsubscribe(log);
    }

    public static void unhookErr(Logger log)
    {
        errHook.unsubscribe(log);
    }

    private void printThroughLoggers(String s)
    {
        for (Logger log : this.subscribedLoggers)
        {
            int oldCallerIndex = log.getCallerStackIndex();

            if (this == outHook)
            {
                log.setCallerStackIndex(this.currentCallerIndex);
                log.print(s);
            }
            else
            {
                log.setCallerStackIndex(this.currentCallerIndex + 1);
                log.printErr(s);
            }

            log.setCallerStackIndex(oldCallerIndex);
        }
    }

    @Override
    public void print(boolean b)
    {
        printThroughLoggers(String.valueOf(b));
        if (this.forwardOutput)
        {
            super.print(b);
        }
        this.currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    }

    @Override
    public void print(char c)
    {
        printThroughLoggers(String.valueOf(c));
        if (this.forwardOutput)
        {
            super.print(c);
        }
        this.currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    }

    @Override
    public void print(int i)
    {
        printThroughLoggers(String.valueOf(i));
        if (this.forwardOutput)
        {
            super.print(i);
        }
        this.currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    }

    @Override
    public void print(long l)
    {
        printThroughLoggers(String.valueOf(l));
        if (this.forwardOutput)
        {
            super.print(l);
        }
        this.currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    }

    @Override
    public void print(float f)
    {
        printThroughLoggers(String.valueOf(f));
        if (this.forwardOutput)
        {
            super.print(f);
        }
        this.currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    }

    @Override
    public void print(double d)
    {
        printThroughLoggers(String.valueOf(d));
        if (this.forwardOutput)
        {
            super.print(d);
        }
        this.currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    }

    @Override
    public void print(char s[])
    {
        printThroughLoggers(String.valueOf(s));
        if (this.forwardOutput)
        {
            super.print(s);
        }
        this.currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    }

    @Override
    public void print(String s)
    {
        printThroughLoggers(String.valueOf(s));
        if (this.forwardOutput)
        {
            super.print(s);
        }
        this.currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    }

    @Override
    public void print(Object obj)
    {
        printThroughLoggers(String.valueOf(obj));
        if (this.forwardOutput)
        {
            super.print(obj);
        }
        this.currentCallerIndex = PRINT_CALLER_STACK_INDEX;
    }

    @Override
    public void println()
    {
        this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
        if (this.forwardOutput)
        {
            super.println("");
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print("");
        }

    }

    @Override
    public void println(boolean x)
    {
        if (this.forwardOutput)
        {
            this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
            super.println(x);
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print(x);
        }
    }

    @Override
    public void println(char x)
    {
        if (this.forwardOutput)
        {
            this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
            super.println(x);
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print(x);
        }
    }

    @Override
    public void println(int x)
    {
        if (this.forwardOutput)
        {
            this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
            super.println(x);
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print(x);
        }
    }

    @Override
    public void println(long x)
    {
        if (this.forwardOutput)
        {
            this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
            super.println(x);
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print(x);
        }
    }

    @Override
    public void println(float x)
    {
        if (this.forwardOutput)
        {
            this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
            super.println(x);
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print(x);
        }
    }

    @Override
    public void println(double x)
    {
        if (this.forwardOutput)
        {
            this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
            super.println(x);
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print(x);
        }
    }

    @Override
    public void println(char x[])
    {
        if (this.forwardOutput)
        {
            this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
            super.println(x);
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print(x);
        }
    }

    @Override
    public void println(String x)
    {
        if (this.forwardOutput)
        {
            this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
            super.println(x);
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print(x);
        }
    }

    @Override
    public void println(Object x)
    {
        if (this.forwardOutput)
        {
            this.currentCallerIndex = PRINTLN_CALLER_STACK_INDEX;
            super.println(x);
        }
        else
        {
            this.currentCallerIndex = NON_FORWARD_CALLER_STACK_INDEX;
            print(x);
        }
    }

    public void printToOut(String s)
    {
        super.print(s);
        super.print(System.lineSeparator());
    }

    public void printToErr(Throwable t)
    {
        t.printStackTrace(this.outStream);
    }

    @Override
    public PrintStream printf(String format, Object... args)
    {
        for (Logger log : this.subscribedLoggers)
        {
            log.printf(format, args);
        }

        return super.format(format, args);
    }
}