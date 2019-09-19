package bt.types.number;

/**
 * @author &#8904
 *
 */
public class MutableInt
{
    private int i;

    public MutableInt(int i)
    {
        this.i = i;
    }

    public void add(int j)
    {
        this.i += j;
    }

    public void set(int j)
    {
        this.i = j;
    }

    public int get()
    {
        return this.i;
    }
}