package bt.utils.console;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author &#8904
 *
 */
public class ConsoleTable
{
    private ConsoleFormatter formatter;
    private List<ConsoleRow> rows = new ArrayList<>();
    private ConsoleRow titleRow;
    private int rowLength = -1;

    public ConsoleTable(int... format)
    {
        this.formatter = new ConsoleFormatter(format);
    }

    public ConsoleTable(ConsoleFormatter formatter)
    {
        this.formatter = formatter;
    }

    public void addRow(Object... data)
    {
        ConsoleRow row = this.formatter.formatRow(data);
        this.rows.add(row);
        this.rowLength = row.length();
    }

    public void addRow(boolean centered, Object... data)
    {
        ConsoleRow row = this.formatter.formatRow(data, centered);
        this.rows.add(row);
        this.rowLength = row.length();
    }

    public void addTitle(Object... data)
    {
        ConsoleRow row = this.formatter.formatRow(data);
        this.titleRow = row;
        this.rowLength = row.length();
    }

    public void addTitle(boolean centered, Object... data)
    {
        ConsoleRow row = this.formatter.formatRow(data, centered);
        this.titleRow = row;
        this.rowLength = row.length();
    }

    public void removeRow(int index)
    {
        this.rows.remove(index);
    }

    /**
     * Returns the row at the given index (zero index based, top down). The title row can be accessed by passing -1 as
     * index.
     * 
     * @param index
     *            The index of the row. -1 for the title row.
     * @return
     */
    private ConsoleRow rowAtIndex(int index)
    {
        ConsoleRow row;

        if (index == -1)
        {
            row = this.titleRow;
        }
        else
        {
            row = this.rows.get(index);
        }

        return row;
    }

    /**
     * Returns the data array of the row at the given index (zero index based, top down). The title row can be accessed
     * by passing -1 as index.
     * 
     * @param index
     *            The index of the row. -1 for the title row.
     * @return The data array of the selected row.
     */
    public Object[] getData(int index)
    {
        return rowAtIndex(index).getData();
    }

    /**
     * Sets the data array of the row at the given index (zero index based, top down). The title row can be accessed by
     * passing -1 as index.
     * 
     * <p>
     * The passed data array must have the same length as the used format of this instance.
     * </p>
     * 
     * @param index
     *            The index of the row. -1 for the title row.
     * @param data
     *            The new data array.
     */
    public void setData(int index, Object... data)
    {
        rowAtIndex(index).setData(data);
    }

    /**
     * Returns whether the row at the given index (zero index based, top down) is centered. The title row can be
     * accessed by passing -1 as index.
     * 
     * @param index
     *            The index of the row. -1 for the title row.
     * @return true if the row is centered, false otherwise.
     */
    public boolean isCentered(int index)
    {
        return rowAtIndex(index).isCentered();
    }

    /**
     * Sets whether the values of the row at the given index (zero index based, top down) will be centered during
     * formatting. The title row can be accessed by passing -1 as index.
     * 
     * @param index
     *            The index of the row. -1 for the title row.
     * @param centered
     *            true to center the row in the future.
     */
    public void setCentered(int index, boolean centered)
    {
        rowAtIndex(index).setCentered(centered);
    }

    public void setConsoleFormatter(ConsoleFormatter formatter)
    {
        this.formatter = formatter;

        for (ConsoleRow row : this.rows)
        {
            row.setFormatter(formatter);
            row.setupAfterChange();
        }
    }

    public void reformat()
    {
        for (ConsoleRow row : this.rows)
        {
            row.setupAfterChange();
        }
    }

    public void print(PrintStream out)
    {
        String separator = "";
        String titleSeparator = "";

        for (int i = 0; i < this.rowLength; i ++ )
        {
            separator += this.formatter.getRowSeparator();
            titleSeparator += this.formatter.getTitleSeparator();
        }

        out.println(separator);

        if (this.titleRow != null)
        {
            out.println(this.titleRow);
            out.println(titleSeparator);
        }

        for (ConsoleRow row : this.rows)
        {
            out.println(row);
            out.println(separator);
        }
    }

    public String getFullRowSeparator()
    {
        String separator = "";

        for (int i = 0; i < this.rowLength; i ++ )
        {
            separator += this.formatter.getRowSeparator();
        }

        return separator;
    }

    @Override
    public String toString()
    {
        String output = "";

        String separator = "";
        String titleSeparator = "";

        for (int i = 0; i < this.rowLength; i ++ )
        {
            separator += this.formatter.getRowSeparator();
            titleSeparator += this.formatter.getTitleSeparator();
        }

        output += separator + "\n";

        if (this.titleRow != null)
        {
            output += this.titleRow + "\n";
            output += titleSeparator + "\n";
        }

        for (ConsoleRow row : this.rows)
        {
            output += row + "\n";
            output += separator + "\n";
        }

        return output;
    }
}