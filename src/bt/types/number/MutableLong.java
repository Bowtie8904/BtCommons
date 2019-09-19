package bt.types.number;

/**
 * @author &#8904
 *
 */
public class MutableLong
{
    private long l;

    public MutableLong(long l)
    {
        this.l = l;
    }

    public void add(long j)
    {
        this.l += j;
    }

    public void set(long j)
    {
        this.l = j;
    }

    public long get()
    {
        return this.l;
    }
}