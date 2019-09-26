package bt.utils.date;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * @author &#8904
 *
 */
public final class DateUtils
{
    public static Date addDays(Date date, int days)
    {
        date.setTime(date.getTime() + TimeUnit.DAYS.toMillis(days));
        return date;
    }

    public static Time addDays(Time time, int days)
    {
        time.setTime(time.getTime() + TimeUnit.DAYS.toMillis(days));
        return time;
    }

    public static Timestamp addDays(Timestamp timestamp, int days)
    {
        timestamp.setTime(timestamp.getTime() + TimeUnit.DAYS.toMillis(days));
        return timestamp;
    }

    public static Date addHours(Date date, int hours)
    {
        date.setTime(date.getTime() + TimeUnit.HOURS.toMillis(hours));
        return date;
    }

    public static Time addHours(Time time, int hours)
    {
        time.setTime(time.getTime() + TimeUnit.HOURS.toMillis(hours));
        return time;
    }

    public static Timestamp addHours(Timestamp timestamp, int hours)
    {
        timestamp.setTime(timestamp.getTime() + TimeUnit.HOURS.toMillis(hours));
        return timestamp;
    }

    public static Date addMinutes(Date date, int minutes)
    {
        date.setTime(date.getTime() + TimeUnit.MINUTES.toMillis(minutes));
        return date;
    }

    public static Time addMinutes(Time time, int minutes)
    {
        time.setTime(time.getTime() + TimeUnit.MINUTES.toMillis(minutes));
        return time;
    }

    public static Timestamp addMinutes(Timestamp timestamp, int minutes)
    {
        timestamp.setTime(timestamp.getTime() + TimeUnit.MINUTES.toMillis(minutes));
        return timestamp;
    }

    public static Date addSeconds(Date date, int seconds)
    {
        date.setTime(date.getTime() + TimeUnit.SECONDS.toMillis(seconds));
        return date;
    }

    public static Time addSeconds(Time time, int seconds)
    {
        time.setTime(time.getTime() + TimeUnit.SECONDS.toMillis(seconds));
        return time;
    }

    public static Timestamp addSeconds(Timestamp timestamp, int seconds)
    {
        timestamp.setTime(timestamp.getTime() + TimeUnit.SECONDS.toMillis(seconds));
        return timestamp;
    }
}