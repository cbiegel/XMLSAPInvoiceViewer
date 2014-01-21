package xmlviewer.tree.utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import xmlviewer.tree.XMLIgnoreWhitespaceFilter;


/**
 * This class provides utility methods for the DOM component of Java.
 * 
 * @author cbiegel
 */
public class DOMUtil {

    /**
     * Create a new Document (XML file) with the given InputSource.
     * 
     * @param input
     *            The InputSource that contains the XML file (in an InputStream) to be converted into a Document
     * @return A Document of the XML file (with all the nodes set up)
     */
    public static Document createDocument(InputSource input)
            throws TransformerException, ParserConfigurationException, SAXException {

        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        // set NameSpaceAware to true in order to prevent an exception
        saxFactory.setNamespaceAware(true);
        SAXParser parser = saxFactory.newSAXParser();
        XMLReader reader = new XMLIgnoreWhitespaceFilter(parser.getXMLReader());

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        DOMResult result = new DOMResult();
        transformer.transform(new SAXSource(reader, input), result);
        System.out.println(result.getNode().getFirstChild().getFirstChild().getFirstChild().getNodeName());
        return (Document) result.getNode();
    }

}
