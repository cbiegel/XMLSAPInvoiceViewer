package tests.tree.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
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
        String[] subElementList =
            DetailedViewUtil.getSubElementsListFromTree(_sapInvoiceExternalElements.getFirstChild(), true, false);

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
        DetailedViewUtil.getSubElementsListFromTree(null, true, false);

        Map<Integer, Node> nodeMap = DetailedViewUtil.getNodeMap();

        assertEquals(0, nodeMap.size());

        // modify the static node map in DetailedViewUtil
        testGetSubElementsListFromTree();

        nodeMap = DetailedViewUtil.getNodeMap();

        assertEquals(3, nodeMap.size());
    }

    @Test
    public void testGetCompactAttributes()
    {
        // amount + currency
        String[][] testData = { {"priceAmount", "200"}, {"priceCurrency", "EUR"}};
        String[][] result = DetailedViewUtil.getCompactAttributes(testData);
        assertArrayEquals(new String[][] {{"priceAmount", "200 EUR"}}, result);

        // date + time (start)
        testData = new String[][] { {"dateOfEvent", "20141234"}, {"timeOfEvent", "1234"}};
        result = DetailedViewUtil.getCompactAttributes(testData);
        assertArrayEquals(new String[][] {{"dateAndTimeOfEvent", "34.12.2014 - 12:34"}}, result);

        // date + time (end)
        testData = new String[][] { {"eventDate", "20140317"}, {"eventTime", "1500"}};
        result = DetailedViewUtil.getCompactAttributes(testData);
        assertArrayEquals(new String[][] {{"eventDateAndTime", "17.03.2014 - 15:00"}}, result);

        // time + date (end)
        testData = new String[][] { {"Mauerfall", "true"}, {"mauerfallTime", "2115"}, {"mauerfallDate", "19891109"}};
        result = DetailedViewUtil.getCompactAttributes(testData);
        assertArrayEquals(new String[][] { {"Mauerfall", "true"}, {"mauerfallDateAndTime", "09.11.1989 - 21:15"}}, result);

        // starts with date
        testData = new String[][] { {"dateOfEvent", "20000101"}, {"test", "true"}};
        result = DetailedViewUtil.getCompactAttributes(testData);
        assertArrayEquals(new String[][] { {"dateOfEvent", "01.01.2000"}, {"test", "true"}}, result);

        // ends with date
        testData = new String[][] { {"eventDate", "20000101"}, {"test", "true"}};
        result = DetailedViewUtil.getCompactAttributes(testData);
        assertArrayEquals(new String[][] { {"eventDate", "01.01.2000"}, {"test", "true"}}, result);

        // ends with time
        testData = new String[][] { {"test", "test"}, {"eventTime", "1523"}};
        result = DetailedViewUtil.getCompactAttributes(testData);
        assertArrayEquals(new String[][] { {"test", "test"}, {"eventTime", "15:23"}}, result);

        // regular elements
        testData = new String[][] { {"foo", "bar"}, {"hello", "world"}, {"bob", "alice"}};
        result = DetailedViewUtil.getCompactAttributes(testData);
        assertArrayEquals(new String[][] { {"foo", "bar"}, {"hello", "world"}, {"bob", "alice"}}, result);
    }

    @Test
    public void testGetTableData()
    {
        JTable table = new JTable();
        String[][] testData = new String[][] { {"foo", "bar"}, {"hello", "world"}, {"bob", "alice"}};
        table.setModel(new DefaultTableModel(testData, new String[] {"Attributes", "Values"}));
        String[][] data = DetailedViewUtil.getTableData(table);
        assertArrayEquals(testData, data);
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

    private void assertArrayEquals(String[][] expected, String[][] actual)
    {
        if (expected == null && actual == null)
        {
            return;
        }

        // We test to see if the first dimension is the same.
        if (expected.length != actual.length) {
            fail("The array lengths of the first dimensions aren't the same.");
        }

        // We test every array inside the 'outer' array.
        for (int i = 0; i < expected.length; i++)
        {
            assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

}
