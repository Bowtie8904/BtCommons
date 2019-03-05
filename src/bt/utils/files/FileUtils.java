package bt.utils.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;

import bt.utils.log.Logger;

/**
 * @author &#8904
 *
 */
public class FileUtils
{
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

    public static File getJarDirectory(Class c)
    {
        try
        {
            return new File(c.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        }
        catch (URISyntaxException e)
        {
            Logger.global().print(e);
        }
        return null;
    }

    public static String readFile(String path)
    {
        return readFile(new File(path));
    }

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

    public static File[] getFiles(String directoryPath)
    {
        File dir = new File(directoryPath);

        if (!dir.isDirectory())
        {
            throw new IllegalArgumentException("Path " + directoryPath + " does not point to a directory.");
        }

        return dir.listFiles();
    }

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
                return name.toLowerCase().endsWith("." + ending);
            }
        };

        File[] files = dir.listFiles(filter);

        return files;
    }
}