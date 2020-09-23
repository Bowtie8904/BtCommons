package bt.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Implementes utilities for modifying collections.
 *
 * @author &#8904
 */
public final class CollectionUtils
{
    /**
     * Adds the given non-null object to the given collection if it is an instance of the given class or any of its
     * subclasses.
     *
     * @param <T>
     *            The element type of the given collection.
     * @param cls
     *            The class type that is used to check whether the given object should be added.
     * @param collection
     *            The collection to add the object to.
     * @param obj
     *            The object which will be added to the given collection if it is an instance of the given class type.
     * @return true if the object was added, false otherwise.
     */
    public static <T> boolean addIfInstanceOf(Class<? extends T> cls, Collection<T> collection, Object obj)
    {
        if (cls.isInstance(obj))
        {
            return collection.add(cls.cast(obj));
        }

        return false;
    }

    /**
     * Attempts to add the given object to the given collection.
     *
     * @param <T>
     *            The element type of the given collection.
     * @param collection
     *            The collection to add the object to.
     * @param obj
     *            The object which will be added to the given collection if it can be cast to T without throwing a
     *            {@link ClassCastException}.
     * @return true if the object was added, false otherwise.
     */
    public static <T> boolean tryAdd(Collection<T> collection, Object obj)
    {
        try
        {
            return collection.add((T)obj);
        }
        catch (ClassCastException e)
        {
            return false;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     *
     *
     * @param <T>
     *            The element type of the given collection.
     * @param condition
     * @param collection
     *            The collection to add the object to.
     * @param obj
     *            The object which will be added to the given collection if the given suppliers {@link Supplier#get()
     *            get} function returns true.
     * @return true if the object was added, false otherwise.
     */
    public static <T> boolean addIf(Supplier<Boolean> condition, Collection<T> collection, T obj)
    {
        if (condition.get().booleanValue())
        {
            return collection.add(obj);
        }

        return false;
    }

    /**
     * Attempts to find the first key in the given map that maps to the given instance so that
     * <code>Objects.equals(map.get(key), value)</code> returns true.
     *
     * @param <K>
     * @param <V>
     * @param map
     * @param value
     * @return The first found key or null if no key was found.
     */
    public static <K, V> K getKeyForValue(Map<K, V> map, V value)
    {
        K key = map.keySet()
                   .stream()
                   .filter(k -> Objects.equals(map.get(k), value))
                   .findFirst().orElse(null);

        return key;
    }

    /**
     * Attempts to find all keys in the given map that map to the given instance so that
     * <code>Objects.equals(map.get(key), value)</code> returns true.
     *
     * @param <K>
     * @param <V>
     * @param map
     * @param value
     * @return A list containing all found keys. The list may be empty but will never be null.
     */
    public static <K, V> List<K> getKeysForValue(Map<K, V> map, V value)
    {
        var keys = map.keySet()
                      .stream()
                      .filter(k -> Objects.equals(map.get(k), value))
                      .collect(Collectors.toList());

        return keys;
    }
}