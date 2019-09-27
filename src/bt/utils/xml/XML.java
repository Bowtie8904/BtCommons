package bt.utils.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import bt.utils.log.Logger;

/**
 * @author &#8904
 *
 */
public class XML
{
    public static Document parse(String xml)
    {
        Document doc = null;
        try
        {
            doc = DocumentHelper.parseText(xml);
        }
        catch (DocumentException e)
        {
            Logger.global().print(e);
        }
        return doc;
    }

    public static Document parse(File xml)
    {
        if (xml == null)
        {
            return null;
        }

        SAXReader reader = new SAXReader();
        Document doc = null;

        try
        {
            doc = reader.read(xml);
        }
        catch (DocumentException e)
        {
            Logger.global().print(e);
        }

        return doc;
    }

    public static Document parse(InputStream xml)
    {
        if (xml == null)
        {
            return null;
        }

        SAXReader reader = new SAXReader();
        Document doc = null;

        try
        {
            doc = reader.read(xml);
        }
        catch (DocumentException e)
        {
            Logger.global().print(e);
        }

        return doc;
    }

    public static void save(Document doc, File file)
    {
        try
        {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        catch (IOException e)
        {
            Logger.global().print(e);
        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        try (FileWriter fileWriter = new FileWriter(file))
        {
            XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
            xmlWriter.write(doc);
        }
        catch (IOException e)
        {
            Logger.global().print(e);
        }
    }

    public static void save(Document doc, String filePath)
    {
        save(doc, new File(filePath));
    }

    public static ElementBuilder element(Element e)
    {
        return new ElementBuilder(e);
    }

    public static ElementBuilder element(String name)
    {
        return new ElementBuilder(name);
    }

    public static ElementBuilder element(String name, String text)
    {
        return new ElementBuilder(name, text);
    }

    public static XMLBuilder builder()
    {
        return new XMLBuilder();
    }

    public static XMLBuilder builder(Document doc)
    {
        return new XMLBuilder(doc);
    }

    /**
     * XPath attribute conversion to be case-insensitive.
     *
     * @param attribute
     * @return
     */
    public static String lowerAttribute(String attribute)
    {
        return "@*[lower-case(local-name()) = '" + attribute.toLowerCase() + "']";
    }

    /**
     * XPath node conversion to be case-insensitive.
     *
     * @param node
     * @return
     */
    public static String lowerNode(String node)
    {
        return "*[lower-case(local-name()) = '" + node.toLowerCase() + "']";
    }
}