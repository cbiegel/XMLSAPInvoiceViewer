package xmlviewer.ui.tools;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xmlviewer.tree.XMLTreeExpansionState;
import xmlviewer.tree.util.XMLTreeUtil;
import xmlviewer.ui.detail.DetailedView;
import xmlviewer.ui.main.MainWindow;
import xmlviewer.ui.main.XMLViewerFileChooser;


/**
 * The tool for the main UI frame. Contains logic such as listeners and preferences for the UI.
 * 
 * @author cbiegel
 */
public class MainWindowTool {

    private final MainWindow _ui;

    private int _x_pos;
    private int _y_pos;
    private int _width;
    private int _height;

    /**
     * Represents the current view mode.
     * 0 = tree view
     * 1 = detailed view
     */
    private int _viewMode;

    private boolean _saveWindowPosition;
    private boolean _saveTreeState;

    private String _currentFilePath;

    private String _treeExpansionState;

    public MainWindowTool()
    {
        _currentFilePath = "";

        loadWindowLocationProperties();
        loadSettings();

        _ui = new MainWindow(_x_pos, _y_pos, _width, _height, _saveWindowPosition, _saveTreeState);

        setupListeners();
    }

    /**
     * Adds several listeners to the UI's components
     */
    private void setupListeners()
    {
        JMenuItem openItem = _ui.getViewerMenu().getOpenItem();
        JMenuItem exitItem = _ui.getViewerMenu().getExitItem();
        JFrame frame = _ui.getFrame();
        JCheckBoxMenuItem saveWindowLocationCheck = _ui.getViewerMenu().getSettingsSaveWindowLocationItem();
        JCheckBoxMenuItem saveTreeState = _ui.getViewerMenu().getSettingsSaveTreeStateItem();
        JMenuItem collapseAll = _ui.getViewerMenu().getViewCollapseAllItem();
        JMenuItem expandAll = _ui.getViewerMenu().getViewExpandAllItem();
        JMenuItem switchViews = _ui.getViewerMenu().getViewSwitchViewsItem();

        addOpenItemListener(openItem);
        addExitItemListener(exitItem);
        addWindowListener(frame);
        addWindowLocationCheckListener(saveWindowLocationCheck);
        addSaveTreeStateListener(saveTreeState);
        addCollapseAllListener(collapseAll);
        addExpandAllListener(expandAll);
        addSwitchViewsListener(switchViews);
    }

    /**
     * Add a listener to the given JMenuItem. If the item is clicked, a file chooser dialog will be opened.
     * 
     * @param openItem
     *            The JMenuItem to receive a listener.
     */
    private void addOpenItemListener(JMenuItem openItem)
    {
        openItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                XMLViewerFileChooser filechooser = new XMLViewerFileChooser(_ui.getFrame());

                _currentFilePath = filechooser.getLastFilePath();

                if (_saveTreeState)
                {
                    loadTreeExpansionState();
                }

                try
                {
                    // a file has been opened in the FileChooser
                    if (!_currentFilePath.equals("") && filechooser.hasPathChanged())
                    {
                        InputStream ist = new FileInputStream(new File(_currentFilePath));
                        InputSource is = new InputSource(ist);
                        String fileName = getFileNameFromPathName(_currentFilePath);
                        _ui.initializeAndDisplayTree(is, fileName, _treeExpansionState);

                        // enable the menu components
                        JCheckBoxMenuItem saveTreeState = _ui.getViewerMenu().getSettingsSaveTreeStateItem();
                        JMenuItem switchViews = _ui.getViewerMenu().getViewSwitchViewsItem();
                        JMenuItem collapseAll = _ui.getViewerMenu().getViewCollapseAllItem();
                        JMenuItem expandAll = _ui.getViewerMenu().getViewExpandAllItem();

                        if (!saveTreeState.isEnabled())
                            saveTreeState.setEnabled(true);
                        if (!switchViews.isEnabled() && XMLTreeUtil.isTsSapInvoiceListExternal(_ui.getTree()))
                        {
                            switchViews.setEnabled(true);
                        }
                        else if (switchViews.isEnabled() && !XMLTreeUtil.isTsSapInvoiceListExternal(_ui.getTree()))
                        {
                            switchViews.setEnabled(false);
                        }
                        if (!collapseAll.isEnabled())
                            collapseAll.setEnabled(true);
                        if (!expandAll.isEnabled())
                            expandAll.setEnabled(true);

                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addExitItemListener(final JMenuItem item)
    {
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                _ui.getFrame().dispose();
            }
        });
    }

    /**
     * Adds a listener to the given JFrame. If the window is closed and the save position check box has been selected,
     * the program will save the last position (coordinates and width/height) of the window to restore it on the next
     * launch.
     * 
     * @param frame
     *            The JFrame to receive a listener.
     */
    private void addWindowListener(final JFrame frame)
    {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event)
            {
                // save the current location of the window so it restores this state on the next start of the program
                if (_saveWindowPosition)
                {
                    saveWindowPosition();
                }

                if (_saveTreeState)
                {
                    saveTreeExpansionState();
                }
            }

        });
    }

    /**
     * Adds a listener to the given JCheckBoxMenuItem. If the check box is selected, the program will save the state of
     * the frame on closing the window.
     * 
     * @param checkBox
     *            The JCheckBoxMenuItem to receive a listener.
     */
    private void addWindowLocationCheckListener(final JCheckBoxMenuItem checkBox)
    {
        checkBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                _saveWindowPosition = checkBox.isSelected();
                saveSetting(1);
            }
        });
    }

    private void addSaveTreeStateListener(final JCheckBoxMenuItem checkBox)
    {
        checkBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                _saveTreeState = checkBox.isSelected();
                saveSetting(0);
            }

        });
    }

    private void addCollapseAllListener(JMenuItem collapseItem)
    {
        collapseItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                XMLTreeUtil.collapseAllNodes(_ui.getTree());
            }
        });
    }

    private void addExpandAllListener(JMenuItem expandItem)
    {
        expandItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                XMLTreeUtil.expandAllNodes(_ui.getTree());
            }
        });
    }

    private void addSwitchViewsListener(JMenuItem switchViewsItem)
    {
        switchViewsItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                // switch from tree view to detailed view
                if (_viewMode == 0)
                {
                    // save the current tree's state
                    saveTreeExpansionState();

                    DetailedViewTool detailViewTool = new DetailedViewTool(_ui.getTree(), _ui);
                    _ui.initializeAndDisplayDetailedView(detailViewTool.getUI());

                    // adjust the main frame
                    adjustFrameToDetailedView();

                    _ui.getViewerMenu().getViewCollapseAllItem().setEnabled(false);
                    _ui.getViewerMenu().getViewExpandAllItem().setEnabled(false);
                    _ui.getViewerMenu().getSettingsSaveTreeStateItem().setEnabled(false);
                    _ui.getViewerMenu().getViewSwitchViewsItem().setText("Switch to tree view");

                    _viewMode = 1;
                }
                // switch from detailed view to tree view
                else
                {
                    try
                    {
                        InputStream ist = new FileInputStream(new File(_currentFilePath));
                        InputSource is = new InputSource(ist);
                        String fileName = getFileNameFromPathName(_currentFilePath);

                        loadTreeExpansionState();

                        _ui.initializeAndDisplayTree(is, fileName, _treeExpansionState);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }

                    _ui.getViewerMenu().getViewCollapseAllItem().setEnabled(true);
                    _ui.getViewerMenu().getViewExpandAllItem().setEnabled(true);
                    _ui.getViewerMenu().getSettingsSaveTreeStateItem().setEnabled(true);
                    _ui.getViewerMenu().getViewSwitchViewsItem().setText("Switch to detailed view");

                    _viewMode = 0;
                }
            }
        });
    }

    /**
     * Saves the current window position and size.
     * 
     * @param x
     *            The current position of the frame on the x axis.
     * @param y
     *            The current position of the frame on the y axis.
     * @param width
     *            The current width of the frame.
     * @param height
     *            The current height of the frame.
     */
    private void saveWindowLocationProperties(int x, int y, int width, int height)
    {
        UserPreferences.saveWindowPositionX(x);
        UserPreferences.saveWindowPositionY(y);
        UserPreferences.saveWindowWidth(width);
        UserPreferences.saveWindowHeight(height);
    }

    /**
     * Loads the previously saved window location and size. If this is the first start, default values (0) will be used
     * instead.
     */
    private void loadWindowLocationProperties()
    {
        _x_pos = UserPreferences.loadWindowPositionX();
        _y_pos = UserPreferences.loadWindowPositionY();
        _width = UserPreferences.loadWindowWidth();
        _height = UserPreferences.loadWindowHeight();
    }

    /**
     * Loads all settings
     */
    private void loadSettings()
    {
        _saveTreeState = UserPreferences.loadSaveTreeStateProperty();
        _saveWindowPosition = UserPreferences.loadSaveWindowPositionProperty();
    }

    /**
     * Saves the given setting.
     * 
     * @param settingCode
     *            The setting to be saved as an integer representation: 0 = saveTreeState, 1 = saveWindowPosition
     */
    private void saveSetting(int settingCode)
    {
        switch (settingCode)
        {
        case 0:
            UserPreferences.saveSaveTreeStateProperty(_saveTreeState);
            return;
        case 1:
            UserPreferences.saveSaveWindowPositionProperty(_saveWindowPosition);
            return;
        default:
            return;
        }
    }

    private void loadTreeExpansionState()
    {
        int fileNameHash = _currentFilePath.hashCode();

        XMLTreeExpansionState expState = XMLTreeUtil.deserializeTreeExpansionState(fileNameHash);

        if (expState != null)
        {
            _treeExpansionState = expState.getExpansionState();
        }
        else
        {
            // initial expansion state. All nodes are collapsed (except for the root node which is represented by 0)
            _treeExpansionState = ",0";
        }
    }

    private void saveWindowPosition()
    {
        int x = _ui.getFrame().getX();
        int y = _ui.getFrame().getY();
        int width = _ui.getFrame().getWidth();
        int height = _ui.getFrame().getHeight();

        saveWindowLocationProperties(x, y, width, height);
    }

    private void saveTreeExpansionState()
    {
        JTree tree = _ui.getTree();

        if (tree == null)
        {
            return;
        }

        String expState = XMLTreeUtil.getTreeExpansionState(tree, 0);

        XMLTreeExpansionState expansionState = new XMLTreeExpansionState(expState);
        int fileNameHash = _currentFilePath.hashCode();

        XMLTreeUtil.serializeTreeExpansionState(expansionState, fileNameHash);
    }

    /**
     * @param pathName
     *            The path name to be converted to its file name.
     * @return Returns only the file name of a path (the part after the last '\').
     */
    private String getFileNameFromPathName(String pathName)
    {
        int indexOfLastSlash = pathName.lastIndexOf("\\");
        return pathName.substring(indexOfLastSlash + 1);
    }

    private void adjustFrameToDetailedView()
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width / 2) - (DetailedView.DETAILED_VIEW_DEFAULT_WIDTH / 2);
        int y = (dim.height / 2) - (DetailedView.DETAILED_VIEW_DEFAULT_HEIGHT / 2);

        if (_ui.getFrame().getWidth() < DetailedView.DETAILED_VIEW_DEFAULT_WIDTH
            && _ui.getFrame().getHeight() < DetailedView.DETAILED_VIEW_DEFAULT_HEIGHT)
        {
            _ui.getFrame().setBounds(
                x,
                y,
                DetailedView.DETAILED_VIEW_DEFAULT_WIDTH,
                DetailedView.DETAILED_VIEW_DEFAULT_HEIGHT);
        }
        else if (_ui.getFrame().getWidth() < DetailedView.DETAILED_VIEW_DEFAULT_WIDTH)
        {
            int displayHeightHalf = (dim.height / 2);
            int frameHeightHalf = (_ui.getFrame().getHeight() / 2);
            y = displayHeightHalf - frameHeightHalf;

            _ui.getFrame().setBounds(
                x,
                y,
                DetailedView.DETAILED_VIEW_DEFAULT_WIDTH,
                _ui.getFrame().getHeight());
        }
        else if (_ui.getFrame().getHeight() < DetailedView.DETAILED_VIEW_DEFAULT_HEIGHT)
        {
            x = (dim.width / 2) - (_ui.getFrame().getWidth() / 2);

            _ui.getFrame().setBounds(
                x,
                y,
                _ui.getFrame().getWidth(),
                DetailedView.DETAILED_VIEW_DEFAULT_HEIGHT);
        }
    }

    /**
     * Closes the window and disposes all of its UI components.
     */
    public void closeWindow()
    {
        _ui.getFrame().dispose();
    }

    public MainWindow getUI()
    {
        return _ui;
    }

    /**
     * THIS METHOD SERVES FOR TESTING PURPOSES ONLY. YOU SHOULD NOT CALL THIS METHOD IN REGULAR DEVLOPMENT
     * 
     * @param path
     *            Set the _currentFilePath to path
     */
    public void setCurrentFilePath(String path)
    {
        _currentFilePath = path;
    }

}
