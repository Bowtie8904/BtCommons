package bt.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import bt.utils.log.Logger;
import bt.utils.refl.field.Fields;

/**
 * @author &#8904
 *
 */
public interface Copyable<T>
{
    /**
     * Creates a field value identical copy of this instance.
     *
     * @param <T>
     *
     * @return
     */
    default public T copy()
    {
        T copy = null;
        try
        {
            Constructor<T> construct = (Constructor<T>)getClass().getConstructor();
            construct.setAccessible(true);
            copy = construct.newInstance();

            for (var field : Fields.getAllFields(getClass()))
            {
                field.setAccessible(true);
                field.set(copy, field.get(this));
            }
        }
        catch (InstantiationException | IllegalAccessException
               | InvocationTargetException | SecurityException | NoSuchMethodException e1)
        {
            Logger.global().print(e1);
        }

        return copy;
    }

    /**
     * Applies the field values of the given instance to this instances field.
     *
     * @param <T>
     *
     * @param valueProvider
     */
    default public void apply(T valueProvider)
    {
        for (var field : Fields.getAllFields(getClass()))
        {
            try
            {
                field.setAccessible(true);
                field.set(this, field.get(valueProvider));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                Logger.global().print(e);
            }
        }
    }
}