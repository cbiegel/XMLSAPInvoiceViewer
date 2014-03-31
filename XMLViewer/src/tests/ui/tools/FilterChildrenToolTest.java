package tests.ui.tools;

import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import xmlviewer.tree.XMLTreeModel;
import xmlviewer.tree.util.DetailedViewUtil;
import xmlviewer.ui.main.MainWindow;
import xmlviewer.ui.tools.FilterChildrenTool;


public class FilterChildrenToolTest
{

    private static final String TEST_FILE_PATH = ".\\TestResources\\testElement.xml";
    private Node _parentNode;

    @Before
    public void setUp()
            throws Exception
    {

        InputStream testInputStream = new FileInputStream(TEST_FILE_PATH);
        InputSource testInputSource = new InputSource(testInputStream);
        XMLTreeModel testModel = new XMLTreeModel(testInputSource);

        Node root = (Node) testModel.getRoot();
        NodeList children = root.getChildNodes();
        Node sapInvoiceExternals = children.item(3);
        Node headers = sapInvoiceExternals.getFirstChild().getChildNodes().item(2);
        Node items = headers.getFirstChild().getChildNodes().item(2);
        _parentNode = items;
    }

    @Test
    public void test()
    {
        DetailedViewUtil.getSubElementsListFromTree(_parentNode, true, false);
        MainWindow window = new MainWindow(0, 0, 0, 0, false, false);
        @SuppressWarnings("unused")
        FilterChildrenTool tool = new FilterChildrenTool(_parentNode, 0, window.getFrame());

        window.closeWindow();
    }
}
