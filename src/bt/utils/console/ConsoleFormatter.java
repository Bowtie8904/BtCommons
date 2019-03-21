package bt.utils.console;

/**
 * @author &#8904
 *
 */
public class ConsoleFormatter
{
    private int[] format;
    private String columnSeparator = "|";
    private char rowSeparator = '-';
    private char titleSeparator = '=';

    public ConsoleFormatter(int... format)
    {
        this.format = format;
    }

    public ConsoleRow formatRow(Object[] data)
    {
        return formatRow(data, false);
    }

    public ConsoleRow formatRow(Object[] data, boolean centered)
    {
        if (data.length != this.format.length)
        {
            throw new IllegalArgumentException("Format and text array must be the same length.");
        }

        Object[] dataCopy = new Object[data.length];

        for (int i = 0; i < data.length; i ++ )
        {
            dataCopy[i] = data[i] == null ? "null" : data[i];
        }

        String row = this.columnSeparator;
        for (int i = 0; i < dataCopy.length; i ++ )
        {
            if (dataCopy[i].toString().length() > this.format[i] - 2)
            {
                if (this.format[i] == 0)
                {
                    throw new IllegalArgumentException(
                            "Format " + this.format[i] + " at position " + i + " is invalid. Formats must be above 0.");
                }
                if (this.format[i] <= 3)
                {
                    dataCopy[i] = "";
                }
                else if (this.format[i] <= 5)
                {
                    dataCopy[i] = "...";
                }
                else
                {
                    dataCopy[i] = dataCopy[i].toString().substring(0, this.format[i] - 5) + "...";
                }
            }
            int spaces = (int)((this.format[i] - dataCopy[i].toString().length()) / 2);
            if (centered)
            {
                for (int j = 0; j < spaces; j ++ )
                {
                    row += " ";
                }
                row += dataCopy[i];
                spaces = this.format[i] - dataCopy[i].toString().length() - spaces;
                for (int j = 0; j < spaces; j ++ )
                {
                    row += " ";
                }
            }
            else
            {
                row += " ";
                row += dataCopy[i];
                spaces = this.format[i] - dataCopy[i].toString().length() - 1;
                for (int j = 0; j < spaces; j ++ )
                {
                    row += " ";
                }
            }
            row += this.columnSeparator;
        }

        return new ConsoleRow(this, row, data, centered);
    }

    /**
     * @return the format
     */
    public int[] getFormat()
    {
        return this.format;
    }

    /**
     * @param format
     *            the format to set
     */
    public void setFormat(int... format)
    {
        this.format = format;
    }

    /**
     * @return the rowSeparator
     */
    public char getRowSeparator()
    {
        return rowSeparator;
    }

    /**
     * @param rowSeparator
     *            the rowSeparator to set
     */
    public void setRowSeparator(char rowSeparator)
    {
        this.rowSeparator = rowSeparator;
    }

    public char getTitleSeparator()
    {
        return titleSeparator;
    }

    /**
     * @param rowSeparator
     *            the rowSeparator to set
     */
    public void setTitleSeparator(char titleSeparator)
    {
        this.titleSeparator = titleSeparator;
    }

    /**
     * @return the columnSeparator
     */
    public String getColumnSeparator()
    {
        return columnSeparator;
    }

    /**
     * @param columnSeparator
     *            the columnSeparator to set
     */
    public void setColumnSeparator(String columnSeparator)
    {
        this.columnSeparator = columnSeparator;
    }
}