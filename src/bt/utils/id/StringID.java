package bt.utils.id;

import java.util.UUID;

/**
 * @author &#8904
 *
 */
public final class StringID
{
    public static String uniqueID()
    {
        return UUID.randomUUID().toString();
    }
}