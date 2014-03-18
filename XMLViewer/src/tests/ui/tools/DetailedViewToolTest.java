package tests.ui.tools;

import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JTree;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import xmlviewer.tree.XMLTreeModel;
import xmlviewer.ui.detail.DetailedView;
import xmlviewer.ui.main.MainWindow;
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
        DetailedViewTool tool = new DetailedViewTool(_tree, new MainWindow(0, 0, 0, 0, false, false));
        @SuppressWarnings("unused")
        DetailedView ui = tool.getUI();
    }

    // The following tests don't test functionality but rather serve the purpose of catching errors that might occur
    // when the user uses the UI

    @Test
    public void testListenersNoError()
    {
        MainWindow window = new MainWindow(0, 0, 0, 0, false, false);
        DetailedViewTool tool = new DetailedViewTool(_tree, window);
        DetailedView ui = tool.getUI();

        ui.getElementComboBox().setSelectedIndex(1);
        ui.getElementChildrenList().setSelectedIndex(1);
        // activate and deactivate the filter
        window.getViewerMenu().getViewApplyFilterItem().setEnabled(true);
        window.getViewerMenu().getViewApplyFilterItem().setSelected(true);
        ui.getElementChildrenList().setSelectedIndex(2);
        window.getViewerMenu().getViewApplyFilterItem().setSelected(false);
        ui.getElementChildrenList().setSelectedIndex(1);
        window.getViewerMenu().getViewShowCompactItem().setSelected(true);
        window.getViewerMenu().getViewShowCompactItem().setSelected(false);
    }
}
