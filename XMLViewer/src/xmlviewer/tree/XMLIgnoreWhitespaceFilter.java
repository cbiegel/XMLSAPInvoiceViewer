package xmlviewer.tree;

import java.io.CharArrayWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;


/**
 * This class represents a special case of an XMLFilter. With this filter, all whitespace characters are supposed to be
 * ignored in the representation of an XML file.
 * 
 * @author cbiegel
 */
public class XMLIgnoreWhitespaceFilter extends XMLFilterImpl {

    private CharArrayWriter contents = new CharArrayWriter();

    public XMLIgnoreWhitespaceFilter(XMLReader parent) {
        super(parent);
    }

    @Override
    public void startElement(String uri, String localNAme, String qName, Attributes atts)
            throws SAXException
    {
        writeContents();
        super.startElement(uri, localNAme, qName, atts);
    }

    @Override
    public void characters(char ch[], int start, int length) {
        contents.write(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        writeContents();
        super.endElement(uri, localName, qName);
    }

    /**
     * Write the contents
     */
    private void writeContents()
            throws SAXException {
        char ch[] = contents.toCharArray();

        if (!isWhiteSpace(ch))
        {
            super.characters(ch, 0, ch.length);
        }
        contents.reset();
    }

    /**
     * @param ch
     *            The char array to check for whitespaces
     * @return true if ch contains at least one whitespace character, false if no character is a whitespace character.
     */
    private boolean isWhiteSpace(char ch[]) {
        for (int i = 0; i < ch.length; i++)
        {
            if (!Character.isWhitespace(ch[i]))
            {
                return false;
            }
        }
        return true;
    }

    public void ignorableWhitespace(char ch[], int start, int length) {
    }
}
