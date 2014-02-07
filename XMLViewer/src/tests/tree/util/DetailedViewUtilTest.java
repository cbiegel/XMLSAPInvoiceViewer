package tests.tree.util;

import static org.junit.Assert.assertEquals;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import javax.swing.JTree;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import xmlviewer.tree.XMLTreeModel;
import xmlviewer.tree.util.DetailedViewUtil;


public class DetailedViewUtilTest
{
    private static final String TEST_FILE_PATH = ".\\TestResources\\xmlExport.xml";
    private JTree _tree;
    private Node _sapInvoiceExternalElements;

    @Before
    public void setUp()
            throws Exception
    {
        InputStream testInputStream = new FileInputStream(TEST_FILE_PATH);
        InputSource testInputSource = new InputSource(testInputStream);
        XMLTreeModel testModel = new XMLTreeModel(testInputSource);
        _tree = new JTree(testModel);
        Node root = (Node) testModel.getRoot();
        NodeList children = root.getChildNodes();
        _sapInvoiceExternalElements = children.item(3);
    }

    @Test
    public void testGetElementItemsFromTree()
    {
        String[] elementItems = DetailedViewUtil.getElementItemsFromTree(_tree);

        assertEquals(4, elementItems.length);
        assertEquals("Meta Info", elementItems[0]);
        assertEquals("Element 1", elementItems[1]);
        assertEquals("Element 2", elementItems[2]);
        assertEquals("Element 3", elementItems[3]);
    }

    @Test
    public void testGetSubElementsListFromTree()
    {
        String[] subElementList = DetailedViewUtil.getSubElementsListFromTree(_sapInvoiceExternalElements.getFirstChild());

        String[] strippedStrings = stripStringArrayToLetters(subElementList);

        assertEquals(3, subElementList.length);
        assertEquals("subElement", strippedStrings[0]);
        assertEquals("subSubElement", strippedStrings[1]);
        assertEquals("subSubElement", strippedStrings[2]);
    }

    @Test
    public void testGetDetailsForSubElement()
    {
        String[][] subElementDetails =
            DetailedViewUtil.getDetailsForSubElement(_sapInvoiceExternalElements.getFirstChild().getFirstChild());

        for (String[] strings : subElementDetails)
        {
            assertEquals(2, strings.length);
        }

        assertEquals("subElementAttribute1", subElementDetails[0][0]);
        assertEquals("1", subElementDetails[0][1]);
        assertEquals("subElementAttribute2", subElementDetails[1][0]);
        assertEquals("2", subElementDetails[1][1]);
    }

    @Test
    public void testGetNodeMap()
    {
        // reset the static node map in DetailedViewUtil
        DetailedViewUtil.getSubElementsListFromTree(null);

        Map<String, Node> nodeMap = DetailedViewUtil.getNodeMap();

        assertEquals(0, nodeMap.size());

        // modify the static node map in DetailedViewUtil
        testGetSubElementsListFromTree();

        nodeMap = DetailedViewUtil.getNodeMap();

        assertEquals(3, nodeMap.size());
    }

    private String[] stripStringArrayToLetters(String[] arr)
    {
        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++)
        {
            String s = arr[i];
            String res = "";

            for (int c = 0; c < s.length(); c++)
            {
                if (Character.isLetter(s.charAt(c)))
                {
                    res += s.charAt(c);
                }
            }
            result[i] = res;
        }

        return result;
    }

}
