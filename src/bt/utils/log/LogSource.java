package bt.utils.log;

/**
 * @author &#8904
 *
 */
public class LogSource
{
    private Object source;
    private String name;
    
    protected LogSource(Object source, String name)
    {
        this.source = source;
        this.name = name;
    }
    
    public Object getSource()
    {
        return this.source;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getClassName()
    {
        return this.source.getClass().getName();
    }
}