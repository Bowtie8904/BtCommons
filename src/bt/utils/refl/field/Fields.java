package bt.utils.refl.field;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author &#8904
 *
 */
public final class Fields
{
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