package xmlviewer.ui.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
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

    private Enumeration<TreePath> _treeExpansionState;

    public MainWindowTool()
    {
        loadWindowLocationProperties();
        loadSettings();
        loadTreeExpansionState();

        _ui = new MainWindow(_x_pos, _y_pos, _width, _height, _saveWindowPosition);

        setupListeners();
    }

    /**
     * Adds several listeners to the UI's components
     */
    private void setupListeners()
    {
        JMenuItem openItem = _ui.getViewerMenu().getOpenItem();
        JFrame frame = _ui.getFrame();
        JCheckBoxMenuItem saveWindowLocationCheck = _ui.getViewerMenu().getSaveWindowLocationItem();

        addOpenItemListener(openItem);
        addWindowListener(frame);
        addWindowLocationCheckListener(saveWindowLocationCheck);
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

                String chosenFilePath = filechooser.getLastFilePath();

                try
                {
                    if (!chosenFilePath.equals("") && filechooser.hasPathChanged())
                    {
                        InputStream ist = new FileInputStream(new File(chosenFilePath));
                        InputSource is = new InputSource(ist);
                        String fileName = getFileNameFromPathName(chosenFilePath);
                        _ui.initializeAndDisplayTree(is, fileName, _treeExpansionState);
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
                    int x = _ui.getFrame().getX();
                    int y = _ui.getFrame().getY();
                    int width = _ui.getFrame().getWidth();
                    int height = _ui.getFrame().getHeight();

                    saveWindowLocationProperties(x, y, width, height);
                }

                JTree tree = _ui.getTree();
                Enumeration<TreePath> expState = XMLTreeUtil.getTreeExpansionState(tree);
                XMLTreeExpansionState expansionState = new XMLTreeExpansionState(expState);

                XMLTreeUtil.serializeTreeExpansionState(expansionState);
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
                saveSettings(_saveWindowPosition);
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
        _saveWindowPosition = UserPreferences.loadSaveWindowPositionProperty();
    }

    /**
     * Saves all settings
     * 
     * @param saveWindowPos
     *            Determines whether or not the position and size of the window will be saved.
     */
    private void saveSettings(boolean saveWindowPos)
    {
        UserPreferences.saveSaveWindowPositionProperty(saveWindowPos);
    }

    private void loadTreeExpansionState()
    {
        XMLTreeExpansionState expState = XMLTreeUtil.deserializeTreeExpansionState();

        if (expState != null)
        {
            _treeExpansionState = expState.getExpansionState();
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
