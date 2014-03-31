package tests.ui.tools;

import static org.junit.Assert.assertEquals;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import xmlviewer.tree.XMLTreeModel;
import xmlviewer.ui.detail.DetailedViewPanel;
import xmlviewer.ui.main.MainWindow;
import xmlviewer.ui.tools.DetailedViewTool;
import xmlviewer.ui.tools.FindElementTool;


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
        DetailedViewPanel ui = tool.getUI();

        tool.closeWindow();
    }

    @Test
    public void testFindElement()
    {
        MainWindow window = new MainWindow(0, 0, 0, 0, false, false);
        DetailedViewTool dvTool = new DetailedViewTool(_tree, window);
        dvTool.getUI().getElementComboBox().setSelectedIndex(1);
        JList elementList = dvTool.getUI().getElementChildrenList();
        JTable elementTable = dvTool.getUI().getElementDetailTable();
        window.getViewerMenu().getViewFindItem().doClick();
        FindElementTool feTool = dvTool.getFindElementTool();
        feTool.setSearchAttributes(true);

        assertEquals(-1, elementList.getSelectedIndex());
        assertEquals(-1, elementTable.getSelectedRow());
        feTool.searchElement("subelement");
        assertEquals(0, elementList.getSelectedIndex());
        assertEquals(-1, elementTable.getSelectedRow());
        feTool.searchElement("subelement");
        assertEquals(0, elementList.getSelectedIndex());
        assertEquals(0, elementTable.getSelectedRow());
        feTool.searchElement("subelement");
        assertEquals(0, elementList.getSelectedIndex());
        assertEquals(1, elementTable.getSelectedRow());

        for (int c = 0; c < 6; c++)
        {
            feTool.searchElement("subelement");
        }

        assertEquals(2, elementList.getSelectedIndex());
        assertEquals(1, elementTable.getSelectedRow());
        feTool.setWrapSearch(false);
        feTool.searchElement("subelement");
        assertEquals(2, elementList.getSelectedIndex());
        assertEquals(1, elementTable.getSelectedRow());
        feTool.setWrapSearch(true);
        feTool.searchElement("subelement");
        assertEquals(0, elementList.getSelectedIndex());
        assertEquals(-1, elementTable.getSelectedRow());
        assertEquals("Wrapped search", feTool.getStatusLabelText());

        feTool.setCaseSensitive(true);
        feTool.searchElement("subelement");
        assertEquals(0, elementList.getSelectedIndex());
        assertEquals(-1, elementTable.getSelectedRow());
        assertEquals("No match found", feTool.getStatusLabelText());
        feTool.searchElement("subElement");
        assertEquals(0, elementList.getSelectedIndex());
        assertEquals(0, elementTable.getSelectedRow());

        feTool.setWholeMatch(true);
        feTool.searchElement("subElement");
        assertEquals(0, elementList.getSelectedIndex());
        assertEquals(0, elementTable.getSelectedRow());
        assertEquals("No match found", feTool.getStatusLabelText());
        feTool.searchElement("subElementAttribute2");
        assertEquals(0, elementList.getSelectedIndex());
        assertEquals(1, elementTable.getSelectedRow());

        feTool.setCaseSensitive(false);
        feTool.setSearchAttributes(false);
        feTool.setWholeMatch(false);
        feTool.setWrapSearch(false);

        for (int c = 0; c < 3; c++)
        {
            feTool.searchElement("subelement");
        }

        assertEquals(2, elementList.getSelectedIndex());
        assertEquals("Last match reached", feTool.getStatusLabelText());

        window.closeWindow();
    }

    // The following tests don't test functionality but rather serve the purpose of catching errors that might occur
    // when the user uses the UI

    @Test
    public void testListenersNoError()
    {
        MainWindow window = new MainWindow(0, 0, 0, 0, false, false);
        DetailedViewTool tool = new DetailedViewTool(_tree, window);
        DetailedViewPanel ui = tool.getUI();

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

        window.closeWindow();
    }
}
