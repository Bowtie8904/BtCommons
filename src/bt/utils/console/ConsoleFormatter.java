package bt.utils.console;

/**
 * @author &#8904
 *
 */
public final class ConsoleFormatter
{
    public static ConsoleRow formatRow(String[] text, int[] format, boolean centered, String separator)
    {
        if (text.length != format.length)
        {
            throw new IllegalArgumentException("Format and text array must be the same length.");
        }

        String[] dataCopy = new String[text.length];

        for (int i = 0; i < text.length; i ++ )
        {
            dataCopy[i] = text[i];
        }

        String row = separator;
        for (int i = 0; i < dataCopy.length; i ++ )
        {
            if (dataCopy[i].length() > format[i] - 2)
            {
                if (format[i] == 0)
                {
                    throw new IllegalArgumentException(
                            "Format " + format[i] + " at position " + i + " is invalid. Formats must be above 0.");
                }
                if (format[i] <= 3)
                {
                    dataCopy[i] = "";
                }
                else if (format[i] <= 5)
                {
                    dataCopy[i] = "...";
                }
                else
                {
                    dataCopy[i] = dataCopy[i].substring(0, format[i] - 5) + "...";
                }
            }
            int spaces = (int)((format[i] - dataCopy[i].length()) / 2);
            if (centered)
            {
                for (int j = 0; j < spaces; j ++ )
                {
                    row += " ";
                }
                row += dataCopy[i];
                spaces = format[i] - dataCopy[i].length() - spaces;
                for (int j = 0; j < spaces; j ++ )
                {
                    row += " ";
                }
            }
            else
            {
                row += " ";
                row += dataCopy[i];
                spaces = format[i] - dataCopy[i].length() - 1;
                for (int j = 0; j < spaces; j ++ )
                {
                    row += " ";
                }
            }
            row += separator;
        }
        return new ConsoleRow(row, text, format, centered, separator);
    }
}
