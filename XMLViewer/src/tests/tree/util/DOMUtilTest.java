package tests.tree.util;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xmlviewer.tree.util.DOMUtil;


public class DOMUtilTest {

    private static final String TEST_FILE_PATH = ".\\TestResources\\testTree.xml";

    @Before
    public void setUp()
    {
    }

    @Test
    public void testCreateDocument()
            throws FileNotFoundException, TransformerException, ParserConfigurationException, SAXException
    {
        InputStream ist = new FileInputStream(new File(TEST_FILE_PATH));
        InputSource is = new InputSource(ist);
        Document testDocument = DOMUtil.createDocument(is);

        Node root = (Node) testDocument.getDocumentElement();

        assertEquals("root", root.getNodeName());
        assertEquals(2, root.getChildNodes().getLength());
    }

}
