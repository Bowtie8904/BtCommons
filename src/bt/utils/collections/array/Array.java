package bt.utils.collections.array;

/**
 * Simple array utilities.
 * 
 * @author &#8904
 */
public class Array
{
    /**
     * Creates an array of the given elements.
     * 
     * @param elements
     *            The elements for the array.
     * @return The array.
     */
    public <T> T[] of(T... elements)
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
    public boolean[] of(boolean... elements)
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
    public short[] of(short... elements)
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
    public int[] of(int... elements)
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
    public long[] of(long... elements)
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
    public float[] of(float... elements)
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
    public double[] of(double... elements)
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
    public <T> T[] push(T[] arr, T element)
    {
        T[] newArr = (T[])java.lang.reflect.Array.newInstance(element.getClass(), 1);
        return arr;
    }
}