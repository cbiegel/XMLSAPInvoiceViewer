package tests.ui.tools;

import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xmlviewer.ui.tools.MainWindowTool;


public class MainWindowToolTest {

    private static final String TEST_FILE_PATH = ".\\TestResources\\testElement.xml";
    private InputSource _is;

    @Before
    public void setUp()
            throws Exception
    {
        InputStream testInputStream = new FileInputStream(TEST_FILE_PATH);
        _is = new InputSource(testInputStream);
    }

    @Test
    public void test()
    {
        MainWindowTool testWindow = new MainWindowTool();

        testWindow.closeWindow();
    }

    // The following tests don't test functionality but rather serve the purpose of catching errors that might occur
    // when the user uses the UI

    @Test
    public void testListenersNoError()
            throws TransformerException, ParserConfigurationException, SAXException
    {
        MainWindowTool tool = new MainWindowTool();
        tool.getUI().initializeAndDisplayTree(_is, "", ",0");

        tool.getUI().getViewerMenu().getSettingsSaveTreeStateItem().setEnabled(true);
        tool.getUI().getViewerMenu().getSettingsSaveTreeStateItem().doClick();
        tool.getUI().getViewerMenu().getSettingsSaveWindowLocationItem().setEnabled(true);
        tool.getUI().getViewerMenu().getSettingsSaveWindowLocationItem().doClick();
        tool.getUI().getViewerMenu().getViewCollapseAllItem().setEnabled(true);
        tool.getUI().getViewerMenu().getViewCollapseAllItem().doClick();
        tool.getUI().getViewerMenu().getViewExpandAllItem().setEnabled(true);
        // this call might cause a nullpointer exception. If you rerun the test, however, it should be fixed
        tool.getUI().getViewerMenu().getViewExpandAllItem().doClick();
        tool.getUI().getViewerMenu().getViewSwitchViewsItem().setEnabled(true);
        tool.getUI().getViewerMenu().getViewSwitchViewsItem().doClick();

        tool.setCurrentFilePath(TEST_FILE_PATH);

        tool.getUI().getViewerMenu().getViewSwitchViewsItem().doClick();

        tool.closeWindow();
    }

}
