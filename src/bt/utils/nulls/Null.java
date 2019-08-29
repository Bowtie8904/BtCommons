package bt.utils.nulls;

/**
 * @author &#8904
 *
 */
public final class Null
{
    public static <T> T nvl(T checkValue, T defaultValue)
    {
        return checkValue == null ? defaultValue : checkValue;
    }
}