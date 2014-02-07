package tests.tree;

import java.awt.Component;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JTree;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import xmlviewer.tree.XMLTreeCellRenderer;
import xmlviewer.tree.XMLTreeModel;


public class XMLTreeCellRendererTest
{
    private static final String TEST_FILE_PATH = ".\\TestResources\\testTree.xml";

    private XMLTreeCellRenderer _testRenderer;
    private InputSource _testInputSource;
    private XMLTreeModel _testModel;
    private Node _rootNode;
    private JTree _testTree;

    @Before
    public void setUp()
            throws Exception
    {
        _testRenderer = new XMLTreeCellRenderer();
        InputStream testInputStream = new FileInputStream(TEST_FILE_PATH);
        _testInputSource = new InputSource(testInputStream);
        _testModel = new XMLTreeModel(_testInputSource);
        _testTree = new JTree(_testModel);
        _rootNode = (Node) _testModel.getRoot();
    }

    @Test
    public void test()
    {
        @SuppressWarnings("unused")
        Component testComponent = _testRenderer.getTreeCellRendererComponent
                (_testTree, _rootNode, false, false, false, 0, false);
    }

}
