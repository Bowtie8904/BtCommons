package bt.types;

/**
 * A basic implementation of the {@link Tripple} interface.
 *
 * @author &#8904
 *
 * @param <A>
 *            The type of the key.
 * @param <B>
 *            The type of the first value.
 * @param <C>
 *            The type of the second value.
 */
public class SimpleTripple<A, B, C> implements Tripple<A, B, C>
{
    private A key;
    private B value1;
    private C value2;

    /**
     * Creates a new instance which holds the given values and the given key.
     *
     * @param key
     *            The key of this Tripple.
     * @param value1
     *            The first value of this Tripple.
     * @param value2
     *            The second value of this Tripple.
     */
    public SimpleTripple(A key, B value1, C value2)
    {
        this.key = key;
        this.value1 = value1;
        this.value2 = value2;
    }

    /**
     * @see bowt.types.Tripple#getKey()
     */
    @Override
    public A getKey()
    {
        return this.key;
    }

    /**
     * @see bowt.types.Tripple#getFirstValue()
     */
    @Override
    public B getFirstValue()
    {
        return this.value1;
    }

    /**
     * @see bowt.types.Tripple#getSecondValue()
     */
    @Override
    public C getSecondValue()
    {
        return this.value2;
    }

    /**
     * @see bt.types.Tripple#setKey(java.lang.Object)
     */
    @Override
    public void setKey(A a)
    {
        this.key = a;
    }

    /**
     * @see bt.types.Tripple#setFirstValue(java.lang.Object)
     */
    @Override
    public void setFirstValue(B b)
    {
        this.value1 = b;
    }

    /**
     * @see bt.types.Tripple#setSecondValue(java.lang.Object)
     */
    @Override
    public void setSecondValue(C c)
    {
        this.value2 = c;
    }
}