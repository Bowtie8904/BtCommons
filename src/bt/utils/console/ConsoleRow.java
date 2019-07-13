package bt.utils.console;

/**
 * @author &#8904
 *
 */
public class ConsoleRow
{
    private ConsoleFormatter formatter;
    private String text;
    private Object[] data;
    private boolean centered;

    public ConsoleRow(ConsoleFormatter formatter, String text, Object[] data, boolean centered)
    {
        this.formatter = formatter;
        this.text = text;
        this.data = data;
        this.centered = centered;
    }

    protected void setupAfterChange()
    {
        ConsoleRow newRow = this.formatter.formatRow(this.data,
                                                     this.centered);
        this.text = newRow.toString();
    }

    public void setFormatter(ConsoleFormatter formatter)
    {
        this.formatter = formatter;
        setupAfterChange();
    }

    /**
     * @return the data
     */
    public Object[] getData()
    {
        return this.data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Object... data)
    {
        this.data = data;
        setupAfterChange();
    }

    /**
     * @return the centered
     */
    public boolean isCentered()
    {
        return this.centered;
    }

    /**
     * @param centered
     *            the centered to set
     */
    public void setCentered(boolean centered)
    {
        this.centered = centered;
        setupAfterChange();
    }

    public int length()
    {
        return this.text.length();
    }

    @Override
    public String toString()
    {
        return this.text;
    }
}