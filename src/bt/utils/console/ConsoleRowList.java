package bt.utils.console;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author &#8904
 *
 */
public class ConsoleRowList
{
    private int[] format;
    private boolean centered = false;
    private String columnSeparator = "|";
    private char rowSeparator = '-';
    private char titleSeparator = '=';
    private List<ConsoleRow> rows = new ArrayList<>();
    private ConsoleRow titleRow;
    private int length = -1;

    public ConsoleRowList(int... format)
    {
        this.format = format;
    }

    public ConsoleRow addRow(String... data)
    {
        ConsoleRow row = ConsoleFormatter.formatRow(data, this.format, this.centered, this.columnSeparator);
        this.rows.add(row);
        this.length = row.length();
        return row;
    }

    public ConsoleRow addRow(boolean centered, String... data)
    {
        ConsoleRow row = ConsoleFormatter.formatRow(data, this.format, centered, this.columnSeparator);
        this.rows.add(row);
        this.length = row.length();
        return row;
    }

    public ConsoleRow addTitle(String... data)
    {
        ConsoleRow row = ConsoleFormatter.formatRow(data, this.format, this.centered, this.columnSeparator);
        this.titleRow = row;
        this.length = row.length();
        return row;
    }

    public ConsoleRow addTitle(boolean centered, String... data)
    {
        ConsoleRow row = ConsoleFormatter.formatRow(data, this.format, centered, this.columnSeparator);
        this.titleRow = row;
        this.length = row.length();
        return row;
    }

    public ConsoleRow removeRow(int index)
    {
        return this.rows.remove(index);
    }

    public List<ConsoleRow> getRows()
    {
        return this.rows;
    }

    public void print(PrintStream out)
    {
        String separator = "";
        String titleSeparator = "";

        for (int i = 0; i < length; i ++ )
        {
            separator += rowSeparator;
            titleSeparator += this.titleSeparator;
        }

        out.println(separator);

        if (this.titleRow != null)
        {
            out.println(this.titleRow);
            out.println(titleSeparator);
        }

        for (ConsoleRow row : rows)
        {
            out.println(row);
            out.println(separator);
        }
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

        for (ConsoleRow row : rows)
        {
            row.setFormat(format);
            this.length = row.length();
        }
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

        for (ConsoleRow row : rows)
        {
            row.setCentered(centered);
        }
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

        for (ConsoleRow row : rows)
        {
            row.setSeparator(columnSeparator);
            length = row.length();
        }
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
    public void settitleSeparator(char titleSeparator)
    {
        this.titleSeparator = titleSeparator;
    }

    public String getFullRowSeparator()
    {
        String separator = "";
        for (int i = 0; i < length; i ++ )
        {
            separator += rowSeparator;
        }

        return separator;
    }

    @Override
    public String toString()
    {
        String output = "";

        String separator = "";
        String titleSeparator = "";

        for (int i = 0; i < length; i ++ )
        {
            separator += rowSeparator;
            titleSeparator += this.titleSeparator;
        }

        output += separator + "\n";

        if (this.titleRow != null)
        {
            output += this.titleRow + "\n";
            output += titleSeparator + "\n";
        }

        for (ConsoleRow row : rows)
        {
            output += row + "\n";
            output += separator + "\n";
        }

        return output;
    }
}