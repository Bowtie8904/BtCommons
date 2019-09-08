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

    public static long hexToDecimal(String hex)
    {
        return Long.parseLong(hex.trim(), 16);
    }

    public static String hexToOctal(String hex)
    {
        long dec = Long.parseLong(hex.trim(), 16);
        return Long.toOctalString(dec).toUpperCase();
    }

    public static String hexToBinary(String hex)
    {
        long dec = Long.parseLong(hex.trim(), 16);
        return Long.toBinaryString(dec).toUpperCase();
    }

    public static long binaryToDecimal(String binary)
    {
        return Long.parseLong(binary.trim(), 2);
    }

    public static String binaryToOctal(String binary)
    {
        long dec = Long.parseLong(binary.trim(), 2);
        return Long.toOctalString(dec).toUpperCase();
    }

    public static String binaryToHex(String binary)
    {
        long dec = Long.parseLong(binary.trim(), 2);
        return Long.toHexString(dec).toUpperCase();
    }

    public static long octalToDecimal(String octal)
    {
        return Long.parseLong(octal.trim(), 2);
    }

    public static String octalToBinary(String octal)
    {
        long dec = Long.parseLong(octal.trim(), 2);
        return Long.toBinaryString(dec).toUpperCase();
    }

    public static String octalToHex(String octal)
    {
        long dec = Long.parseLong(octal.trim(), 2);
        return Long.toHexString(dec).toUpperCase();
    }
}