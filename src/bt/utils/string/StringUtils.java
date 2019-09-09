package bt.utils.string;

/**
 * @author &#8904
 *
 */
public class StringUtils
{
    public static String leftPad(String src, int length, String pad)
    {
        if (pad.length() > 1)
        {
            throw new IllegalArgumentException("Padding String can only contain one character.");
        }

        String newStr = "";

        if (src.length() < length)
        {
            for (int i = 0; i < length - src.length(); i ++ )
            {
                newStr += pad;
            }
        }

        newStr += src;

        return newStr;
    }

    public static String rightPad(String src, int length, String pad)
    {
        if (pad.length() > 1)
        {
            throw new IllegalArgumentException("Padding String can only contain one character.");
        }

        String newStr = src;

        if (src.length() < length)
        {
            for (int i = 0; i < length - src.length(); i ++ )
            {
                newStr += pad;
            }
        }

        return newStr;
    }
}