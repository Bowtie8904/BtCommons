package bt.utils.prop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import bt.utils.log.Logger;

/**
 * A utility class to get and set property values.
 * 
 * <h1>Property file format:</h1>
 * <p>
 * In order to be able to read values from property files with this class the file has to be formatted correctly.
 * </p>
 * <ul>
 * 
 * <pre>
 * fieldname1: value1
 * fieldname2: value2
 * </pre>
 * 
 * </ul>
 * <h2>Example of usage:</h2>
 * <ul>
 * 
 * <pre>
 * String value = Properties.getValueOf(&quot;fieldname1&quot;,
 *                                      &quot;properties.txt&quot;);
 * 
 * Properties.setValueOf(&quot;fieldname2&quot;,
 *                       &quot;value3&quot;,
 *                       &quot;properties.txt&quot;);
 * </pre>
 * 
 * </ul>
 * 
 * @author &#8904
 */
public final class Properties
{
    /**
     * The path to the default properties file.
     * <p>
     * 'properties/properties.txt'
     * </p>
     */
    private static final String DEFAULT_PROPERTY_PATH = "properties/properties.txt";

    /**
     * Gets all fieldnames that start with the given fieldstring inside the given file. If the file does not exist it
     * will instead search in the default property file which is defined by {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs similar fieldnames you want to get.
     * @param file
     *            The file which should be searched for the field.
     * @return An array containing all fieldnames whichs start with the given fieldstring.
     * @see #getAllFieldsLike(String)
     * @see #getAllFieldsLike(String, String)
     */
    public static String[] getAllFieldsLike(String field, File file)
    {
        if (!file.exists())
        {
            try
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            catch (Exception e)
            {
                Logger.global().print(e);
            }
        }

        List<String> values = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                                                                          "UTF-8")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith(field))
                {
                    String[] parts = line.split(":");
                    values.add(parts[0].trim());
                }
            }
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }
        return values.toArray(new String[] {});
    }

    /**
     * Gets all fieldnames that start with the given fieldstring inside the file at the given path. If the file does not
     * exist it will instead search in the default property file which is defined by {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs similar fieldnames you want to get.
     * @param path
     *            The path to the file.
     * @return An array containing all fieldnames whichs start with the given fieldstring.
     * @see #getAllFieldsLike(String, File)
     * @see #getAllFieldsLike(String)
     */
    public static String[] getAllFieldsLike(String field, String path)
    {
        return getAllFieldsLike(field,
                                new File(path));
    }

    /**
     * Gets all fieldnames that start with the given fieldstring inside the default property file. If the file does not
     * exist it will instead search in the default property file which is defined by {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs similar fieldnames you want to get.
     * @return An array containing all fieldnames whichs start with the given fieldstring.
     * @see #getAllFieldsLike(String, File)
     * @see #getAllFieldsLike(String, String)
     */
    public static String[] getAllFieldsLike(String field)
    {
        return getAllFieldsLike(field,
                                DEFAULT_PROPERTY_PATH);
    }

    /**
     * Gets all values whichs fieldnames start with the given fieldstring inside the given file. If the file does not
     * exist it will instead search in the default property file which is defined by {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs values you want to get.
     * @param file
     *            The file which should be searched for the field.
     * @return An array containing all Strings whichs fieldnames start with the given fieldstring.
     * @see #getAllLike(String)
     * @see #getAllLike(String, String)
     */
    public static String[] getAllLike(String field, File file)
    {
        if (!file.exists())
        {
            try
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            catch (Exception e)
            {
                Logger.global().print(e);
            }
        }
        List<String> values = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                                                                          "UTF-8")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith(field))
                {
                    String value = line.substring(line.indexOf(":") + 1).trim();
                    values.add(value);
                }
            }
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }
        return values.toArray(new String[] {});
    }

    /**
     * Gets all values whichs fieldnames start with the given fieldstring inside the file at the given path. If the file
     * does not exist it will instead search in the default property file which is defined by
     * {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs values you want to get.
     * @param path
     *            The path to the file.
     * @return An array containing all Strings whichs fieldnames start with the given fieldstring.
     * @see #getAllLike(String, File)
     * @see #getAllLike(String)
     */
    public static String[] getAllLike(String field, String path)
    {
        return getAllLike(field,
                          new File(path));
    }

    /**
     * Gets all values whichs fieldnames start with the given fieldstring inside the default property file. If the file
     * does not exist it will instead search in the default property file which is defined by
     * {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs values you want to get.
     * @return An array containing all Strings whichs fieldnames start with the given fieldstring.
     * @see #getAllLike(String, File)
     * @see #getAllLike(String, String)
     */
    public static String[] getAllLike(String field)
    {
        return getAllLike(field,
                          DEFAULT_PROPERTY_PATH);
    }

    /**
     * Gets the value of the given field inside the given file or null if the field does not exist in the file. If the
     * file does not exist it will instead search in the default property file which is defined by
     * {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs value you want to get.
     * @param file
     *            The file which should be searched for the field.
     * @return The String value of the given field or null if the field does not exist in the given file and the default
     *         property file.
     * @see #getValueOf(String)
     * @see #getValueOf(String, String)
     */
    public static String getValueOf(String field, File file)
    {
        if (!file.exists())
        {
            try
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            catch (Exception e)
            {
                Logger.global().print(e);
            }
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                                                                          "UTF-8")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith(field + ":"))
                {
                    // returns the value of the matching field
                    return line.substring(line.indexOf(":") + 1).trim();
                }
            }
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }
        return null;
    }

    /**
     * Gets the value of the given field inside the file at the given location or null if the field does not exist in
     * the file. If the file does not exist at the given location it will instead search in the default property file
     * which is defined by {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs value you want to get.
     * @param path
     *            The path to a file which should be searched for the field.
     * @return The String value of the given field or null if the field does not exist in the file at the given location
     *         and the default property file.
     * @see #getValueOf(String, File)
     * @see #getValueOf(String)
     */
    public static String getValueOf(String field, String path)
    {
        return getValueOf(field,
                          new File(path));
    }

    /**
     * Gets the value of the given field inside the default property file whichs path is defined by
     * {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs value you want to get.
     * @return The String value of the given field or null if the field does not exist in the default property file.
     * @see #getValueOf(String, File)
     * @see #getValueOf(String, String)
     */
    public static String getValueOf(String field)
    {
        return getValueOf(field,
                          new File(DEFAULT_PROPERTY_PATH));
    }

    /**
     * Updates the value of the given field with the given value inside the given file. If the field does not exist it
     * will be added with the given value. If the file does not exist it will be created.
     * 
     * @param field
     *            The name of the field whichs value should be updated or which should be added.
     * @param value
     *            The new value for the given field.
     * @param file
     *            The file which should be updated.
     * @return true if successfull.
     * @see #setValueOf(String, String)
     * @see #setValueOf(String, String, String)
     */
    public static boolean setValueOf(String field, String value, File file)
    {
        if (!file.exists())
        {
            try
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            catch (Exception e)
            {
                Logger.global().print(e);
                return false;
            }
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                                                                          "UTF-8")))
        {
            String line;
            boolean exists = false;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith(field + ":"))
                {
                    sb.append(field + ": " + value + System.lineSeparator());
                    exists = true;
                }
                else
                {
                    sb.append(line + System.lineSeparator());
                }
            }
            if (!exists)
            {
                // adds the field to the bottom of the file if it doesnt exist yet
                sb.append(field + ": " + value + System.lineSeparator());
            }
        }
        catch (Exception e)
        {
            Logger.global().print(e);
            return false;
        }
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file))))
        {
            writer.print(sb.toString());
        }
        catch (Exception e)
        {
            Logger.global().print(e);
            return false;
        }
        return true;
    }

    /**
     * Updates the value of the given field with the given value inside the file at the given location. If the field
     * does not exist it will be added with the given value. If the file does not exist it will be created.
     * 
     * @param field
     *            The name of the field whichs value should be updated or which should be added.
     * @param value
     *            The new value for the given field.
     * @param path
     *            The path to the file which should be updated.
     * @return true if successfull.
     * @see #setValueOf(String, String)
     * @see #setValueOf(String, String, File)
     */
    public static boolean setValueOf(String field, String value, String path)
    {
        return setValueOf(field,
                          value,
                          new File(path));
    }

    /**
     * Updates the value of the given field with the given value inside the default property file whichs path is defined
     * by {@link #DEFAULT_PROPERTY_PATH}.
     * 
     * @param field
     *            The name of the field whichs value should be updated or which should be added.
     * @param value
     *            The new value for the given field.
     * @return true if successfull.
     * @see #setValueOf(String, String, File)
     * @see #setValueOf(String, String, String)
     */
    public static boolean setValueOf(String field, String value)
    {
        return setValueOf(field,
                          value,
                          new File(DEFAULT_PROPERTY_PATH));
    }
}