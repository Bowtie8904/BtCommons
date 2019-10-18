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
     * Creates a field value identical copy of the given instance.
     *
     * @param <K>
     * @param copyable
     * @return
     */
    public static <K> K copy(K copyable)
    {
        K copy = null;
        try
        {
            Constructor<K> construct = (Constructor<K>)copyable.getClass().getConstructor();
            construct.setAccessible(true);
            copy = construct.newInstance();

            for (var field : Fields.getAllFields(copyable.getClass()))
            {
                field.setAccessible(true);
                field.set(copy, field.get(copyable));
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
     * Creates a field value identical copy of this instance.
     *
     * @param <T>
     *
     * @return
     */
    default public T copy()
    {
        return (T)copy(this);
    }

    /**
     * Applies the field values of the given valueProvider to the valueReceivers fields.
     *
     * @param <K>
     * @param valueReceiver
     *            The instance that should receive the new values.
     * @param valueProvider
     *            The instance whichs values should be passed to the receiver.
     */
    public static <K> void apply(K valueReceiver, K valueProvider)
    {
        for (var field : Fields.getAllFields(valueReceiver.getClass()))
        {
            try
            {
                field.setAccessible(true);
                field.set(valueReceiver, field.get(valueProvider));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                Logger.global().print(e);
            }
        }
    }

    /**
     * Applies the field values of the given instance to this instances fields.
     *
     * @param <T>
     *
     * @param valueProvider
     */
    default public void apply(T valueProvider)
    {
        apply(this, valueProvider);
    }
}