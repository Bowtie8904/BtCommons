package bt.utils.console;

/**
 * @author &#8904
 *
 */
public class ConsoleRow
{
    private String text;
    private String[] data;
    private int[] format;
    private boolean centered;
    private String separator;

    public ConsoleRow(String text, String[] data, int[] format, boolean centered, String separator)
    {
        this.text = text;
        this.format = format;
        this.data = data;
        this.centered = centered;
        this.separator = separator;
    }

    private void setupAfterChange()
    {
        ConsoleRow newRow = ConsoleFormatter.formatRow(this.data, this.format, this.centered, this.separator);
        this.text = newRow.toString();
    }

    /**
     * @return the data
     */
    public String[] getData()
    {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(String... data)
    {
        this.data = data;
        setupAfterChange();
    }

    /**
     * @return the format
     */
    public int[] getFormat()
    {
        return format;
    }

    /**
     * @param format
     *            the format to set
     */
    public void setFormat(int... format)
    {
        this.format = format;
        setupAfterChange();
    }

    /**
     * @return the centered
     */
    public boolean isCentered()
    {
        return centered;
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

    /**
     * @return the separator
     */
    public String getSeparator()
    {
        return separator;
    }

    /**
     * @param separator
     *            the separator to set
     */
    public void setSeparator(String separator)
    {
        this.separator = separator;
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