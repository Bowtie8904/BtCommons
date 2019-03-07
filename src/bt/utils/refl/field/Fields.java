package bt.utils.refl.field;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic utilities involving fields.
 * 
 * @author &#8904
 */
public final class Fields
{
    /**
     * Gets all fields of the given class (and its super class/es).
     * 
     * @param cls
     *            The class to look in.
     * @return The list of fields that were found.
     */
    public static List<Field> getAllFields(Class<?> cls)
    {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = cls;

        while (currentClass != null)
        {
            for (Field field : currentClass.getDeclaredFields())
            {
                fields.add(field);
            }

            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }
}