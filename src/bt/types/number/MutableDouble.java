package bt.types.number;

/**
 * @author &#8904
 *
 */
public class MutableDouble
{
    private double d;

    public MutableDouble(double d)
    {
        this.d = d;
    }

    public void add(double j)
    {
        this.d += j;
    }

    public void set(double j)
    {
        this.d = j;
    }

    public double get()
    {
        return this.d;
    }
}