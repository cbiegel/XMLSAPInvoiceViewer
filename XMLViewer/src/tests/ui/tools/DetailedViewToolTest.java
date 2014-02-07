package tests.ui.tools;

import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JTree;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import xmlviewer.tree.XMLTreeModel;
import xmlviewer.ui.DetailedView;
import xmlviewer.ui.tools.DetailedViewTool;


public class DetailedViewToolTest {

    private static final String TEST_FILE_PATH = ".\\TestResources\\xmlExport.xml";
    private JTree _tree;

    @Before
    public void setUp()
            throws Exception
    {
        InputStream testInputStream = new FileInputStream(TEST_FILE_PATH);
        InputSource testInputSource = new InputSource(testInputStream);
        XMLTreeModel testModel = new XMLTreeModel(testInputSource);
        _tree = new JTree(testModel);

    }

    @Test
    public void test()
    {
        DetailedViewTool tool = new DetailedViewTool(_tree);
        DetailedView ui = tool.getUI();
    }

}
