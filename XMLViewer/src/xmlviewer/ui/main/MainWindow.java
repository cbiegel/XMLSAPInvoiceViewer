package xmlviewer.ui.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xmlviewer.tree.XMLTreeCellRenderer;
import xmlviewer.tree.XMLTreeModel;
import xmlviewer.tree.util.XMLTreeUtil;
import xmlviewer.ui.detail.DetailedViewPanel;


/**
 * This class represents the main window (UI) of the program.
 * 
 * @author cbiegel
 */
public class MainWindow {
    private final JFrame _frame;

    private JTree _tree;

    private JScrollPane _framePanel;

    private final XMLViewerMenu _viewerMenu;

    private final JMenuBar _menuBar;

    private static final int DEFAULT_WIDTH = 400;

    private static final int DEFAULT_HEIGHT = 500;

    // image icon paths

    public static final String ICON_VIEWER_FILE_PATH = "/images/XMLViewerIcon_16_16.png";

    public static final String ICON_TREE_VIEW_PATH = "/images/tree_view_icon.png";

    public static final String ICON_DETAILED_VIEW_PATH = "/images/detailed_view_icon.png";

    public static final String ICON_COLLAPSE_PATH = "/images/collapse_icon.png";

    public static final String ICON_EXPAND_PATH = "/images/expand_icon.png";

    public static final String ICON_FIND_ELEMENT_PATH = "/images/find_element_icon.png";

    public MainWindow(int x, int y, int width, int height, boolean saveWindowPos, boolean saveTreeState)
    {
        _tree = null;

        _frame = new JFrame("XML Viewer");

        _viewerMenu = new XMLViewerMenu(saveWindowPos, saveTreeState);

        _menuBar = _viewerMenu.getMenuBar();

        _framePanel =
            new JScrollPane(_tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setupFrame(x, y, width, height, saveWindowPos);
    }

    /**
     * Sets up the JFrame with the given parameters.
     * 
     * @param x_pos
     *            The position of the frame on the x axis.
     * @param y_pos
     *            The position of the frame on the y axis.
     * @param width
     *            The width of the frame.
     * @param height
     *            The height of the frame.
     * @param saveWindowPos
     *            Sets the value of the "save window position check box"
     */
    private void setupFrame(int x_pos, int y_pos, int width, int height, boolean saveWindowPos)
    {
        _frame.add(_framePanel);
        _frame.setJMenuBar(_menuBar);

        // if saveWindowPos is false, we want to create a frame with default values
        if ((x_pos == 0 && y_pos == 0) || (width == 0 && height == 0) || !saveWindowPos)
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            x_pos = (screenSize.width / 2) - (DEFAULT_WIDTH / 2);
            y_pos = (screenSize.height / 2) - (DEFAULT_HEIGHT / 2);

            width = DEFAULT_WIDTH;
            height = DEFAULT_HEIGHT;
        }
        _frame.setBounds(x_pos, y_pos, width, height);

        _frame.setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource(ICON_VIEWER_FILE_PATH))));

        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setVisible(true);
    }

    /**
     * Sets up a new JTree with the given properties and displays it with a custom XML filter and renderer in the
     * JFrame.
     * 
     * @param is
     *            The InputSource that contains the path to the file.
     * @param pathName
     *            The path name of the XML file.
     */
    public void initializeAndDisplayTree(InputSource is, String pathName, String expansionState)
            throws TransformerException, ParserConfigurationException, SAXException
    {
        // create the tree
        _tree = new JTree(new XMLTreeModel(is));
        _tree.setCellRenderer(new XMLTreeCellRenderer());

        XMLTreeUtil.loadTreeExpansionState(_tree, expansionState, 0);

        _frame.setTitle("XMLViewer -- " + pathName);

        // update the ScrollPane to show the new tree
        _framePanel.setViewportView(_tree);
    }

    public void initializeAndDisplayDetailedView(DetailedViewPanel detail)
    {
        _framePanel.setViewportView(detail);
    }

    public void updateTreeExpansionState(String expansionState)
    {
        XMLTreeUtil.loadTreeExpansionState(_tree, expansionState, 0);
    }

    /**
     * @return The JFrame component of this window.
     */
    public JFrame getFrame()
    {
        return _frame;
    }

    /**
     * @return The JTree component of this window.
     */
    public JTree getTree()
    {
        return _tree;
    }

    /**
     * @return The XMLViewerMenu component of this window.
     */
    public XMLViewerMenu getViewerMenu()
    {
        return _viewerMenu;
    }
}
