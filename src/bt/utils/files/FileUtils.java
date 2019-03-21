package bt.utils.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import bt.utils.log.Logger;

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
        try
        {
            return new File(c.getProtectionDomain().getCodeSource().getLocation().toURI());
        }
        catch (URISyntaxException e)
        {
            Logger.global().print(e);
        }
        return null;
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
     */
    public static String[] readLines(String path)
    {
        return readLines(new File(path));
    }

    /**
     * Reads and returns all lines from the given file.
     * 
     * @param file
     * @return
     */
    public static String[] readLines(File file)
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
        catch (FileNotFoundException e)
        {
            Logger.global().print(e);
        }
        catch (IOException e)
        {
            Logger.global().print(e);
        }

        return lines.toArray(new String[] {});
    }

    /**
     * Reads the entire content of the file with the given path.
     * 
     * @param path
     *            The path to the file.
     * @return The entire content from the file.
     */
    public static String readFile(String path)
    {
        return readFile(new File(path));
    }

    /**
     * Reads the entire content of the given file.
     * 
     * @param file
     *            The file to read.
     * @return The entire content from the file.
     */
    public static String readFile(File file)
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
        catch (FileNotFoundException e)
        {
            Logger.global().print(e);
        }
        catch (IOException e)
        {
            Logger.global().print(e);
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