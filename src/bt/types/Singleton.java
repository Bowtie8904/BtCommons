package bt.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bt.utils.Exceptions;
import bt.utils.Null;

/**
 * @author &#8904
 *
 */
public class Singleton
{
    private static Map<Class<?>, Object> instances;

    public static <T> T of(Class<T> type)
    {
        instances = Null.nullValue(() -> instances,
                                   () -> new ConcurrentHashMap<>());

        return Null.nullValue(() -> (T)instances.get(type),
                              () -> Exceptions.logThrowGet(Singleton::create, type));
    }

    private static <T> T create(Class<T> type) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        T instance = null;

        Constructor<T> constr = type.getDeclaredConstructor();
        constr.setAccessible(true);
        instance = constr.newInstance();
        instances.put(type, instance);

        return instance;
    }
}