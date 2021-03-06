package bt.types;

/**
 * A tri data structure with one key and two values.
 *
 * @author &#8904
 */
public interface Tripple<A, B, C>
{
    /**
     * Gets the key of this Tripple.
     *
     * @return
     */
    public A getKey();

    /**
     * Gets the first value of this Tripple.
     *
     * @return
     */
    public B getFirstValue();

    /**
     * Gets the second value of this Tripple.
     *
     * @return
     */
    public C getSecondValue();

    /**
     * Sets the key of this Tripple.
     */
    public void setKey(A a);

    /**
     * Sets the first value of this Tripple.
     */
    public void setFirstValue(B b);

    /**
     * Sets the second value of this Tripple.
     */
    public void setSecondValue(C c);
}