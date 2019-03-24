package bt.utils.num;

/**
 * @author &#8904
 *
 */
public final class NumberUtils
{
    public static float clamp(float num, float min, float max)
    {
        if (num >= max)
        {
            return max;
        }
        else if (num <= min)
        {
            return min;
        }
        else
        {
            return num;
        }
    }

    public static double clamp(double num, double min, double max)
    {
        if (num >= max)
        {
            return max;
        }
        else if (num <= min)
        {
            return min;
        }
        else
        {
            return num;
        }
    }

    public static int clamp(int num, int min, int max)
    {
        if (num >= max)
        {
            return max;
        }
        else if (num <= min)
        {
            return min;
        }
        else
        {
            return num;
        }
    }

    public static long clamp(long num, long min, long max)
    {
        if (num >= max)
        {
            return max;
        }
        else if (num <= min)
        {
            return min;
        }
        else
        {
            return num;
        }
    }
}