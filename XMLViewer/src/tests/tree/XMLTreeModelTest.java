package tests.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import xmlviewer.tree.XMLTreeModel;


public class XMLTreeModelTest
{

    private static final String TEST_FILE_PATH = ".\\TestResources\\testTree.xml";

    private XMLTreeModel _testModel;
    private Node _rootNode;
    private Node _note1;

    @Before
    public void setUp()
            throws Exception
    {
        InputStream testInputStream = new FileInputStream(TEST_FILE_PATH);
        InputSource testInputSource = new InputSource(testInputStream);
        _testModel = new XMLTreeModel(testInputSource);
        _rootNode = (Node) _testModel.getRoot();
        _note1 = (Node) _testModel.getChild(_rootNode, 0);
    }

    @Test
    public void testGetChild()
    {
        assertEquals(_rootNode.getNodeName(), "root");

        assertEquals(_note1.getNodeName(), "note");

        Node from1 = (Node) _testModel.getChild(_note1, 0);
        assertEquals(from1.getNodeName(), "from");

        Node fromName1 = (Node) _testModel.getChild(from1, 0);
        assertEquals(fromName1.getNodeValue(), "Foo");

        Node to1 = (Node) _testModel.getChild(_note1, 1);
        assertEquals(to1.getNodeName(), "to");

        Node toName1 = (Node) _testModel.getChild(to1, 0);
        assertEquals(toName1.getNodeValue(), "Bar");

        Node title1 = (Node) _testModel.getChild(_note1, 2);
        assertEquals(title1.getNodeName(), "title");

        Node titleValue1 = (Node) _testModel.getChild(title1, 0);
        assertEquals(titleValue1.getNodeValue(), "Reminder");

        Node message1 = (Node) _testModel.getChild(_note1, 3);
        assertEquals(message1.getNodeName(), "message");

        Node messageValue1 = (Node) _testModel.getChild(message1, 0);
        assertEquals(messageValue1.getNodeValue(), "Don't forget me this weekend");
    }

    @Test
    public void testGetChildCount()
    {
        assertEquals(_testModel.getChildCount(_rootNode), 2);

        Node note1 = (Node) _rootNode.getFirstChild();
        assertEquals(_testModel.getChildCount(note1), 4);

        assertEquals(_testModel.getChildCount(note1), (note1.getChildNodes().getLength() + 2));
    }

    @Test
    public void testIsLeaf()
    {
        assertFalse(_testModel.isLeaf(_rootNode));

        Node child1 = (Node) _testModel.getChild(_rootNode, 0);
        assertFalse(_testModel.isLeaf(child1));

        Node title1 = (Node) _testModel.getChild(_note1, 3);
        Node titleText1 = (Node) _testModel.getChild(title1, 0);

        assertTrue(_testModel.isLeaf(titleText1));
    }

    @Test
    public void testGetIndexOfChild()
    {
        assertEquals(-1, _testModel.getIndexOfChild(_rootNode, _rootNode));

        assertEquals(0, _testModel.getIndexOfChild(_rootNode, _note1));

        assertEquals(1, _testModel.getIndexOfChild(_rootNode, _testModel.getChild(_rootNode, 1)));

        assertEquals(3, _testModel.getIndexOfChild(_note1, _note1.getLastChild()));

        Node from1 = (Node) _testModel.getChild(_note1, 0);

        assertEquals(0, _testModel.getIndexOfChild(_note1, from1));
    }

    @Test
    public void testValueForPathChanged()
    {
        // Not implemented
    }

    @Test
    public void testAddTreeModelListener()
    {
        // Not implemented
    }

    @Test
    public void testRemoveTreeModelListener()
    {
        // Not implemented
    }

}
