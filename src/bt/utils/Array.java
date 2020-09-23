package bt.utils;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * Simple array utilities.
 *
 * @author &#8904
 */
public final class Array
{
    /**
     * Creates an array of the given elements.
     *
     * @param elements
     *            The elements for the array.
     * @return The array.
     */
    public static <T> T[] of(T... elements)
    {
        return elements;
    }

    /**
     * Creates an array of the given elements.
     *
     * @param elements
     *            The elements for the array.
     * @return The array.
     */
    public static boolean[] of(boolean... elements)
    {
        return elements;
    }

    /**
     * Creates an array of the given elements.
     *
     * @param elements
     *            The elements for the array.
     * @return The array.
     */
    public static byte[] of(byte... elements)
    {
        return elements;
    }

    /**
     * Creates an array of the given elements.
     *
     * @param elements
     *            The elements for the array.
     * @return The array.
     */
    public static short[] of(short... elements)
    {
        return elements;
    }

    /**
     * Creates an array of the given elements.
     *
     * @param elements
     *            The elements for the array.
     * @return The array.
     */
    public static int[] of(int... elements)
    {
        return elements;
    }

    /**
     * Creates an array of the given elements.
     *
     * @param elements
     *            The elements for the array.
     * @return The array.
     */
    public static long[] of(long... elements)
    {
        return elements;
    }

    /**
     * Creates an array of the given elements.
     *
     * @param elements
     *            The elements for the array.
     * @return The array.
     */
    public static float[] of(float... elements)
    {
        return elements;
    }

    /**
     * Creates an array of the given elements.
     *
     * @param elements
     *            The elements for the array.
     * @return The array.
     */
    public static double[] of(double... elements)
    {
        return elements;
    }

    /**
     * Pushes the given element into the array.
     *
     * <p>
     * The new element will be at the last index in the array after this operation.
     * </p>
     *
     * @param arr
     *            The base array.
     * @param element
     *            The element to push into the array.
     * @return The new array with a size of arr.length + 1.
     */
    public static <T> T[] push(T[] arr, T element)
    {
        T[] newArr = (T[])create(element.getClass(),
                                 arr.length + 1);

        for (int i = 0; i < arr.length; i ++ )
        {
            newArr[i] = arr[i];
        }

        newArr[newArr.length - 1] = element;

        return newArr;
    }

    /**
     * Pushes the given element into the array.
     *
     * <p>
     * The new element will be at the last index in the array after this operation.
     * </p>
     *
     * @param arr
     *            The base array.
     * @param element
     *            The element to push into the array.
     * @return The new array with a size of arr.length + 1.
     */
    public static boolean[] push(boolean[] arr, boolean element)
    {
        boolean[] newArr = new boolean[arr.length + 1];

        for (int i = 0; i < arr.length; i ++ )
        {
            newArr[i] = arr[i];
        }

        newArr[newArr.length - 1] = element;

        return newArr;
    }

    /**
     * Pushes the given element into the array.
     *
     * <p>
     * The new element will be at the last index in the array after this operation.
     * </p>
     *
     * @param arr
     *            The base array.
     * @param element
     *            The element to push into the array.
     * @return The new array with a size of arr.length + 1.
     */
    public static byte[] push(byte[] arr, byte element)
    {
        byte[] newArr = new byte[arr.length + 1];

        for (int i = 0; i < arr.length; i ++ )
        {
            newArr[i] = arr[i];
        }

        newArr[newArr.length - 1] = element;

        return newArr;
    }

    /**
     * Pushes the given element into the array.
     *
     * <p>
     * The new element will be at the last index in the array after this operation.
     * </p>
     *
     * @param arr
     *            The base array.
     * @param element
     *            The element to push into the array.
     * @return The new array with a size of arr.length + 1.
     */
    public static short[] push(short[] arr, short element)
    {
        short[] newArr = new short[arr.length + 1];

        for (int i = 0; i < arr.length; i ++ )
        {
            newArr[i] = arr[i];
        }

        newArr[newArr.length - 1] = element;

        return newArr;
    }

    /**
     * Pushes the given element into the array.
     *
     * <p>
     * The new element will be at the last index in the array after this operation.
     * </p>
     *
     * @param arr
     *            The base array.
     * @param element
     *            The element to push into the array.
     * @return The new array with a size of arr.length + 1.
     */
    public static int[] push(int[] arr, int element)
    {
        int[] newArr = new int[arr.length + 1];

        for (int i = 0; i < arr.length; i ++ )
        {
            newArr[i] = arr[i];
        }

        newArr[newArr.length - 1] = element;

        return newArr;
    }

    /**
     * Pushes the given element into the array.
     *
     * <p>
     * The new element will be at the last index in the array after this operation.
     * </p>
     *
     * @param arr
     *            The base array.
     * @param element
     *            The element to push into the array.
     * @return The new array with a size of arr.length + 1.
     */
    public static long[] push(long[] arr, long element)
    {
        long[] newArr = new long[arr.length + 1];

        for (int i = 0; i < arr.length; i ++ )
        {
            newArr[i] = arr[i];
        }

        newArr[newArr.length - 1] = element;

        return newArr;
    }

    /**
     * Pushes the given element into the array.
     *
     * <p>
     * The new element will be at the last index in the array after this operation.
     * </p>
     *
     * @param arr
     *            The base array.
     * @param element
     *            The element to push into the array.
     * @return The new array with a size of arr.length + 1.
     */
    public static float[] push(float[] arr, float element)
    {
        float[] newArr = new float[arr.length + 1];

        for (int i = 0; i < arr.length; i ++ )
        {
            newArr[i] = arr[i];
        }

        newArr[newArr.length - 1] = element;

        return newArr;
    }

    /**
     * Pushes the given element into the array.
     *
     * <p>
     * The new element will be at the last index in the array after this operation.
     * </p>
     *
     * @param arr
     *            The base array.
     * @param element
     *            The element to push into the array.
     * @return The new array with a size of arr.length + 1.
     */
    public static double[] push(double[] arr, double element)
    {
        double[] newArr = new double[arr.length + 1];

        for (int i = 0; i < arr.length; i ++ )
        {
            newArr[i] = arr[i];
        }

        newArr[newArr.length - 1] = element;

        return newArr;
    }

    /**
     * Removes the element at the given index and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @param index
     *            The index of the element that should be removed.
     * @return The new array with a size of arr.length - 1.
     */
    public static <T> T[] pop(T[] arr, int index)
    {
        if (arr.length == 0)
        {
            throw new IllegalArgumentException("Can't pop an element from an empty array.");
        }

        T[] newArr = (T[])create(arr[index].getClass(),
                                 arr.length - 1);

        int newArrIndex = 0;

        for (int i = 0; i < arr.length; i ++ )
        {
            if (i != index)
            {
                newArr[newArrIndex] = arr[i];
                newArrIndex ++ ;
            }
        }
        return newArr;
    }

    /**
     * Removes the element at the given index and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @param index
     *            The index of the element that should be removed.
     * @return The new array with a size of arr.length - 1.
     */
    public static boolean[] pop(boolean[] arr, int index)
    {
        if (arr.length == 0)
        {
            throw new IllegalArgumentException("Can't pop an element from an empty array.");
        }

        boolean[] newArr = new boolean[arr.length - 1];

        int newArrIndex = 0;

        for (int i = 0; i < arr.length; i ++ )
        {
            if (i != index)
            {
                newArr[newArrIndex] = arr[i];
                newArrIndex ++ ;
            }
        }
        return newArr;
    }

    /**
     * Removes the element at the given index and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @param index
     *            The index of the element that should be removed.
     * @return The new array with a size of arr.length - 1.
     */
    public static byte[] pop(byte[] arr, int index)
    {
        if (arr.length == 0)
        {
            throw new IllegalArgumentException("Can't pop an element from an empty array.");
        }

        byte[] newArr = new byte[arr.length - 1];

        int newArrIndex = 0;

        for (int i = 0; i < arr.length; i ++ )
        {
            if (i != index)
            {
                newArr[newArrIndex] = arr[i];
                newArrIndex ++ ;
            }
        }
        return newArr;
    }

    /**
     * Removes the element at the given index and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @param index
     *            The index of the element that should be removed.
     * @return The new array with a size of arr.length - 1.
     */
    public static short[] pop(short[] arr, int index)
    {
        if (arr.length == 0)
        {
            throw new IllegalArgumentException("Can't pop an element from an empty array.");
        }

        short[] newArr = new short[arr.length - 1];

        int newArrIndex = 0;

        for (int i = 0; i < arr.length; i ++ )
        {
            if (i != index)
            {
                newArr[newArrIndex] = arr[i];
                newArrIndex ++ ;
            }
        }
        return newArr;
    }

    /**
     * Removes the element at the given index and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @param index
     *            The index of the element that should be removed.
     * @return The new array with a size of arr.length - 1.
     */
    public static int[] pop(int[] arr, int index)
    {
        if (arr.length == 0)
        {
            throw new IllegalArgumentException("Can't pop an element from an empty array.");
        }

        int[] newArr = new int[arr.length - 1];

        int newArrIndex = 0;

        for (int i = 0; i < arr.length; i ++ )
        {
            if (i != index)
            {
                newArr[newArrIndex] = arr[i];
                newArrIndex ++ ;
            }
        }
        return newArr;
    }

    /**
     * Removes the element at the given index and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @param index
     *            The index of the element that should be removed.
     * @return The new array with a size of arr.length - 1.
     */
    public static long[] pop(long[] arr, int index)
    {
        if (arr.length == 0)
        {
            throw new IllegalArgumentException("Can't pop an element from an empty array.");
        }

        long[] newArr = new long[arr.length - 1];

        int newArrIndex = 0;

        for (int i = 0; i < arr.length; i ++ )
        {
            if (i != index)
            {
                newArr[newArrIndex] = arr[i];
                newArrIndex ++ ;
            }
        }
        return newArr;
    }

    /**
     * Removes the element at the given index and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @param index
     *            The index of the element that should be removed.
     * @return The new array with a size of arr.length - 1.
     */
    public static float[] pop(float[] arr, int index)
    {
        if (arr.length == 0)
        {
            throw new IllegalArgumentException("Can't pop an element from an empty array.");
        }

        float[] newArr = new float[arr.length - 1];

        int newArrIndex = 0;

        for (int i = 0; i < arr.length; i ++ )
        {
            if (i != index)
            {
                newArr[newArrIndex] = arr[i];
                newArrIndex ++ ;
            }
        }
        return newArr;
    }

    /**
     * Removes the element at the given index and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @param index
     *            The index of the element that should be removed.
     * @return The new array with a size of arr.length - 1.
     */
    public static double[] pop(double[] arr, int index)
    {
        if (arr.length == 0)
        {
            throw new IllegalArgumentException("Can't pop an element from an empty array.");
        }

        double[] newArr = new double[arr.length - 1];

        int newArrIndex = 0;

        for (int i = 0; i < arr.length; i ++ )
        {
            if (i != index)
            {
                newArr[newArrIndex] = arr[i];
                newArrIndex ++ ;
            }
        }
        return newArr;
    }

    /**
     * Removes the first element and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @return The new array with a size of arr.length - 1.
     */
    public static <T> T[] pop(T[] arr)
    {
        return pop(arr,
                   0);
    }

    /**
     * Removes the first element and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @return The new array with a size of arr.length - 1.
     */
    public static boolean[] pop(boolean[] arr)
    {
        return pop(arr,
                   0);
    }

    /**
     * Removes the first element and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @return The new array with a size of arr.length - 1.
     */
    public static byte[] pop(byte[] arr)
    {
        return pop(arr,
                   0);
    }

    /**
     * Removes the first element and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @return The new array with a size of arr.length - 1.
     */
    public static short[] pop(short[] arr)
    {
        return pop(arr,
                   0);
    }

    /**
     * Removes the first element and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @return The new array with a size of arr.length - 1.
     */
    public static int[] pop(int[] arr)
    {
        return pop(arr,
                   0);
    }

    /**
     * Removes the first element and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @return The new array with a size of arr.length - 1.
     */
    public static long[] pop(long[] arr)
    {
        return pop(arr,
                   0);
    }

    /**
     * Removes the first element and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @return The new array with a size of arr.length - 1.
     */
    public static float[] pop(float[] arr)
    {
        return pop(arr,
                   0);
    }

    /**
     * Removes the first element and returns a new array with reduced size.
     *
     * @param arr
     *            The base array.
     * @return The new array with a size of arr.length - 1.
     */
    public static double[] pop(double[] arr)
    {
        return pop(arr,
                   0);
    }

    /**
     * Creates a new array of the given type with the given size.
     *
     * @param type
     *            The type of the array elements.
     * @param size
     *            The size of the returned array.
     * @return The created array.
     */
    public static <T> T[] create(Class<T> type, int size)
    {
        return (T[])java.lang.reflect.Array.newInstance(type,
                                                        size);
    }

    /**
     * Concatinates both arrays and returns a new one containing all elements.
     *
     * @param <T>
     * @param a1
     *            The first array.
     * @param a2
     *            The second array.
     * @param generator
     *            The array generator, i.e. String[]::new
     * @return The combined array.
     */
    public static <T> T[] concat(T[] a1, T[] a2, IntFunction<T[]> generator)
    {
        return Stream.concat(Arrays.stream(a1), Arrays.stream(a2)).toArray(generator);
    }
}