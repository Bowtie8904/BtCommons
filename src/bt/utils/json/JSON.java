package bt.utils.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * A utility class to perform JSON operations.
 * 
 * @author &#8904
 *
 */
public final class JSON
{
    /**
     * Parses the given JSON String to a valid JSONObject.
     * 
     * @param json
     *            The json String to parse.
     * @return The parsed JSONObject or null if the String was null or incorrectly formatted.
     */
    public static JSONObject parse(String json)
    {
        if (json == null)
        {
            return null;
        }

        JSONTokener tokener = new JSONTokener(json);
        JSONObject object = null;

        try
        {
            object = new JSONObject(tokener);
        }
        catch (JSONException e)
        {
            return null;
        }
        return object;
    }
}