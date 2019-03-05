package bt.types;

/**
 * @author &#8904
 *
 */
public class SimpleTripple<A, B, C> implements Tripple<A, B, C>
{
    private A key;
    private B value1;
    private C value2;

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
}