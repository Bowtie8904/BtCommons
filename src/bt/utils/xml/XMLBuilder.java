package bt.utils.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author &#8904
 *
 */
public class XMLBuilder
{
    private Document doc;

    public XMLBuilder()
    {
        this.doc = DocumentHelper.createDocument();
    }

    public XMLBuilder(Document doc)
    {
        this.doc = doc;
    }

    public XMLBuilder addElement(Element element)
    {
        this.doc.add(element);

        return this;
    }

    public XMLBuilder addElements(Element... elements)
    {
        for (Element e : elements)
        {
            addElement(e);
        }

        return this;
    }

    public XMLBuilder addElement(Document element)
    {
        this.doc.add(element.getRootElement().createCopy());

        return this;
    }

    public XMLBuilder addElements(Document... elements)
    {
        for (Document e : elements)
        {
            addElement(e);
        }

        return this;
    }

    public XMLBuilder addElement(Xmlable element)
    {
        this.doc.add(element.toXML().getRootElement().createCopy());

        return this;
    }

    public XMLBuilder addElements(Xmlable... elements)
    {
        for (Xmlable e : elements)
        {
            addElement(e);
        }

        return this;
    }

    public XMLBuilder addElement(ElementBuilder element)
    {
        this.doc.add(element.toXML());

        return this;
    }

    public XMLBuilder addElements(ElementBuilder... elements)
    {
        for (ElementBuilder e : elements)
        {
            addElement(e);
        }

        return this;
    }

    public ElementBuilder getRoot()
    {
        return XML.element(this.doc.getRootElement());
    }

    public Document toXML()
    {
        return this.doc;
    }

    @Override
    public String toString()
    {
        return this.doc.asXML();
    }
}