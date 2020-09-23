package bt.utils;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.UUID;

/**
 * Utilities for String IDs.
 * 
 * @author &#8904
 */
public final class StringID
{
    private static char[] availableChars;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;
    private static final SplittableRandom random = new SplittableRandom();

    /**
     * Loads all letters from the given charset, so that they can be used by {@link #randomID(int)}.
     * 
     * <p>
     * If this method is not called explicitly, it will be called with the argument {@link StandardCharsets#US_ASCII}.
     * </p>
     * 
     * @param charset
     *            The charset whichs letters should be used for future random IDs.
     */
    public static void loadCharset(Charset charset)
    {
        CharsetEncoder enc = charset.newEncoder();
        List<Character> characters = new ArrayList<Character>();

        for (char c = 0; c < Character.MAX_VALUE; c ++ )
        {
            if (enc.canEncode(c) && Character.isLetter(c))
            {
                characters.add(c);
            }
        }

        char[] tempArr = new char[characters.size()];

        for (int i = 0; i < characters.size(); i ++ )
        {
            tempArr[i] = characters.get(i);
        }

        availableChars = tempArr;
    }

    /**
     * Wrapper method of {@link UUID#randomUUID()} for consistency.
     * 
     * @return
     */
    public static String uniqueID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * Returns a random String with the given length.
     * 
     * <p>
     * The used characters are defined by {@link #loadCharset(Charset)}.
     * </p>
     * 
     * @param length
     * @return
     */
    public static String randomID(int length)
    {
        if (availableChars == null)
        {
            loadCharset(DEFAULT_CHARSET);
        }

        var builder = new StringBuilder();

        for (int i = 0; i < length; i ++ )
        {
            builder.append(availableChars[random.nextInt(availableChars.length)]);
        }

        return builder.toString();
    }
}