package xmlviewer.ui.tools;

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
import xmlviewer.tree.utils.XMLTreeUtil;
import xmlviewer.ui.MainWindow;
import xmlviewer.ui.XMLViewerFileChooser;


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
        JFrame frame = _ui.getFrame();
        JCheckBoxMenuItem saveWindowLocationCheck = _ui.getViewerMenu().getSettingsSaveWindowLocationItem();
        JCheckBoxMenuItem saveTreeState = _ui.getViewerMenu().getSettingsSaveTreeStateItem();
        JMenuItem collapseAll = _ui.getViewerMenu().getViewCollapseAllItem();
        JMenuItem expandAll = _ui.getViewerMenu().getViewExpandAllItem();

        addOpenItemListener(openItem);
        addWindowListener(frame);
        addWindowLocationCheckListener(saveWindowLocationCheck);
        addSaveTreeStateListener(saveTreeState);
        addCollapseAllListener(collapseAll);
        addExpandAllListener(expandAll);
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
                XMLViewerFileChooser filechooser = new XMLViewerFileChooser();

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
                        JMenuItem collapseAll = _ui.getViewerMenu().getViewCollapseAllItem();
                        JMenuItem expandAll = _ui.getViewerMenu().getViewExpandAllItem();

                        if (!saveTreeState.isEnabled())
                            saveTreeState.setEnabled(true);
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
                    saveTreeState();
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

            private void saveTreeState()
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

}
