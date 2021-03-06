package bt.async;

import java.io.Serializable;


/**
 * @author &#8904
 *
 */
public class Data<T> implements Serializable
{
    private final Class<T> dataType;
    private final T data;
    private final String id;
    private long addTime;

    public Data(Class<T> dataType, T data)
    {
        this.dataType = dataType;
        this.data = data;
        this.id = "0";
    }

    public Data(Class<T> dataType, T data, String id)
    {
        this.dataType = dataType;
        this.data = data;
        this.id = id;
    }

    public Data(String id)
    {
        this.dataType = null;
        this.data = null;
        this.id = id;
    }

    public String getID()
    {
        return this.id;
    }

    public T get()
    {
        return this.dataType.cast(this.data);
    }

    public <C> C get(Class<C> type)
    {
        return type.cast(this.data);
    }

    public Class<T> getType()
    {
        return this.dataType;
    }

    /**
     * @return the addTime
     */
    public long getAddTime()
    {
        return this.addTime;
    }

    /**
     * @param addTime
     *            the addTime to set
     */
    public void setAddTime(long addTime)
    {
        this.addTime = addTime;
    }
}