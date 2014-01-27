package tests.tree.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xmlviewer.tree.XMLTreeExpansionState;
import xmlviewer.tree.XMLTreeModel;
import xmlviewer.tree.util.XMLTreeUtil;


public class XMLTreeUtilTest {

    private static final String TEST_FILE_PATH = ".\\TestResources\\testTree.xml";
    private static final String FULLY_EXTENDED_STATE = ",0,1,2,4,6,8,10,11,13,15,17,19";

    private InputSource _testInputSource;
    private XMLTreeModel _testModel;
    private Node _rootNode;
    private JTree _testTree;

    @Before
    public void setUp()
            throws Exception
    {
        InputStream testInputStream = new FileInputStream(TEST_FILE_PATH);
        _testInputSource = new InputSource(testInputStream);
        _testModel = new XMLTreeModel(_testInputSource);
        _testTree = new JTree(_testModel);
        _rootNode = (Node) _testModel.getRoot();
    }

    @Test
    public void testIsDescendant()
    {
        TreePath path1 = new TreePath(_rootNode);
        Object[] path2Array = {_rootNode, _rootNode.getFirstChild()};
        TreePath path2 = new TreePath(path2Array);

        assertFalse(XMLTreeUtil.isDescendant(path1, path1));
        assertTrue(XMLTreeUtil.isDescendant(path2, path1));
    }

    @Test
    public void testGetTreeExpansionState()
            throws TransformerException, ParserConfigurationException, SAXException
    {
        // initial expansion state
        assertEquals(",0", XMLTreeUtil.getTreeExpansionState(_testTree, 0));

        // first row expanded
        _testTree.expandRow(1);
        assertEquals(",0,1", XMLTreeUtil.getTreeExpansionState(_testTree, 0));

        // all rows expanded
        for (int c = 0; c < _testTree.getRowCount(); c++)
        {
            _testTree.expandRow(c);
        }

        assertEquals(FULLY_EXTENDED_STATE, XMLTreeUtil.getTreeExpansionState(_testTree, 0));
    }

    @Test
    public void testLoadTreeExpansionState()
    {
        // test for null
        assertEquals(",0", XMLTreeUtil.getTreeExpansionState(_testTree, 0));
        XMLTreeUtil.loadTreeExpansionState(_testTree, null, 0);
        assertEquals(",0", XMLTreeUtil.getTreeExpansionState(_testTree, 0));

        XMLTreeUtil.loadTreeExpansionState(_testTree, ",0,1,2,4,6,8,10", 0);
        assertEquals(",0,1,2,4,6,8,10", XMLTreeUtil.getTreeExpansionState(_testTree, 0));
    }

    @Test
    public void testExpandAndCollapseAllNodes()
    {
        assertEquals(",0", XMLTreeUtil.getTreeExpansionState(_testTree, 0));
        XMLTreeUtil.expandAllNodes(_testTree);
        assertEquals(FULLY_EXTENDED_STATE, XMLTreeUtil.getTreeExpansionState(_testTree, 0));
        XMLTreeUtil.collapseAllNodes(_testTree);
        assertEquals(",0", XMLTreeUtil.getTreeExpansionState(_testTree, 0));
    }

    @Test
    public void testSerializeAndDeserializeTreeExpansionState()
    {
        int filePathHash = TEST_FILE_PATH.hashCode();
        String treeExpansionPath = ".\\expansionStates";
        String treeExpansionFile = treeExpansionPath + "\\treeExp-";
        String treeExpansionFileSuffix = ".exp";
        String outputFile = treeExpansionFile + filePathHash + treeExpansionFileSuffix;

        // test for null
        XMLTreeExpansionState expState = new XMLTreeExpansionState(null);
        XMLTreeUtil.serializeTreeExpansionState(expState, filePathHash);
        File output = new File(outputFile);

        assertFalse(output.exists());

        XMLTreeUtil.expandAllNodes(_testTree);
        expState = new XMLTreeExpansionState(XMLTreeUtil.getTreeExpansionState(_testTree, 0));
        XMLTreeUtil.serializeTreeExpansionState(expState, filePathHash);
        assertTrue(output.exists());

        // deserialize
        expState = XMLTreeUtil.deserializeTreeExpansionState(filePathHash);
        assertEquals(FULLY_EXTENDED_STATE, expState.getExpansionState());

        // delete the file
        output.delete();
    }
}
