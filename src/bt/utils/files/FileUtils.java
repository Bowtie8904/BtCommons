package bt.utils.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bt.utils.exc.Exceptions;

/**
 * Simple utilities for finding and reading files.
 *
 * @author &#8904
 */
public class FileUtils
{
    /**
     * Gets the jar file that the given class is implemented in.
     *
     * @param c
     *            The class to find the jar file for.
     * @return The file or null if the search failed.
     */
    public static File getJarFile(Class c)
    {
        return Exceptions.logThrowGet(() -> new File(c.getProtectionDomain().getCodeSource().getLocation().toURI()));
    }

    /**
     * Gets the directory that the jar file which implements the given class is in.
     *
     * @param c
     *            The class to find the jar file directory for.
     * @return The directory or null if the search failed.
     */
    public static File getJarDirectory(Class c)
    {
        File jarFile = getJarFile(c);

        if (jarFile != null)
        {
            return jarFile.getParentFile();
        }

        return null;
    }

    /**
     * Reads and returns all lines from the file at the given path.
     *
     * @param path
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String[] readLines(String path) throws FileNotFoundException, IOException
    {
        return readLines(new File(path));
    }

    /**
     * Reads and returns all lines from the given file.
     *
     * @param file
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String[] readLines(File file) throws FileNotFoundException, IOException
    {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                lines.add(line);
            }
        }

        return lines.toArray(new String[] {});
    }

    /**
     * Reads the entire content of the file with the given path.
     *
     * @param path
     *            The path to the file.
     * @return The entire content from the file.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String readFile(String path) throws FileNotFoundException, IOException
    {
        return readFile(new File(path));
    }

    /**
     * Reads the entire content of the given file.
     *
     * @param file
     *            The file to read.
     * @return The entire content from the file.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String readFile(File file) throws FileNotFoundException, IOException
    {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
        }

        return sb.toString();
    }

    /**
     * Gets all files that are in the directory with the given path.
     *
     * @param directoryPath
     *            The path to the directory.
     * @return An array containing all files from the given directory.
     */
    public static File[] getFiles(String directoryPath)
    {
        File dir = new File(directoryPath);

        if (!dir.isDirectory())
        {
            throw new IllegalArgumentException("Path " + directoryPath + " does not point to a directory.");
        }

        return dir.listFiles();
    }

    /**
     * Gets all files with the given file extension that are in the directory with the given path.
     *
     * @param directoryPath
     *            The path to the directory.
     * @param ending
     *            The file extension to filter for.
     * @return An array containing all files with the given file extension from the given directory.
     */
    public static File[] getFiles(String directoryPath, String ending)
    {
        File dir = new File(directoryPath);

        if (!dir.isDirectory())
        {
            throw new IllegalArgumentException("Path " + directoryPath + " does not point to a directory.");
        }

        FilenameFilter filter = new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.toLowerCase().endsWith("." + ending.toLowerCase());
            }
        };

        File[] files = dir.listFiles(filter);

        return files;
    }
}