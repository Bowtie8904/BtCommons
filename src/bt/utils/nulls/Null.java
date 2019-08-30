package bt.utils.nulls;

/**
 * @author &#8904
 *
 */
public final class Null
{
    /**
     * Checks if the first parameter is null and if so, returns the second parameter as default value.
     *
     * @param <T>
     * @param checkValue
     *            The value to be checked for null.
     * @param defaultValue
     *            The default value that is returned if the checkValue is null.
     * @return Either the checkValue if it is not null or the defaultValue.
     */
    public static <T> T nvl(T checkValue, T defaultValue)
    {
        return checkValue == null ? defaultValue : checkValue;
    }
}