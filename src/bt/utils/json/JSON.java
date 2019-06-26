package bt.utils.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import bt.utils.files.FileUtils;
import bt.utils.log.Logger;

/**
 * A utility class to perform JSON operations.
 * 
 * @author &#8904
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

    /**
     * Parses the given JSON InpuitStream to a valid JSONObject.
     * 
     * @param json
     *            The json InputStream to parse.
     * @return The parsed JSONObject or null if the InputStream was null or incorrectly formatted.
     */
    public static JSONObject parse(InputStream json)
    {
        if (json == null)
        {
            return null;
        }

        JSONTokener tokener = new JSONTokener(json);
        JSONObject object = null;

        try
        {
            json.close();
        }
        catch (IOException e1)
        {
            Logger.global().print(e1);
        }

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

    /**
     * Parses the given JSON file to a valid JSONObject.
     * 
     * @param json
     *            The json file to parse.
     * @return The parsed JSONObject or null if the file was null, does not exist or is incorrectly formatted.
     */
    public static JSONObject parse(File json)
    {
        return parse(FileUtils.readFile(json));
    }
}