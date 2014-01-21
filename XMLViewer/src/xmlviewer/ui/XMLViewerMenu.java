package xmlviewer.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


/**
 * This class is a custom menu. It uses a JMenuBar as a top level component and JMenus as children.
 * 
 * @author cbiegel
 */
public class XMLViewerMenu {

    private JMenuBar _menuBar;
    private JMenu _file;
    private JMenu _settings;
    private JMenuItem _open;
    private JCheckBoxMenuItem _saveWindowLocation;

    public XMLViewerMenu(boolean saveWindowPos) {
        _menuBar = new JMenuBar();
        _file = new JMenu(" File ");
        _file.setName("file");

        _settings = new JMenu(" Settings ");
        _settings.setName("settings");

        _open = new JMenuItem("Open...");
        _open.setName("open");

        _saveWindowLocation = new JCheckBoxMenuItem("Save window location on close");
        _saveWindowLocation.setName("saveWindowLocation");
        _saveWindowLocation.setSelected(saveWindowPos);

        setupMenuBar();
    }

    private void setupMenuBar()
    {
        _file.setMnemonic(KeyEvent.VK_F);

        _open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        _saveWindowLocation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));

        _file.add(_open);

        _settings.add(_saveWindowLocation);

        _menuBar.add(_file);
        _menuBar.add(_settings);
    }

    public JMenuBar getMenuBar()
    {
        return _menuBar;
    }

    public JMenu getFileMenu()
    {
        return _file;
    }

    public JMenu getSettingsMenu()
    {
        return _settings;
    }

    public JMenuItem getOpenItem()
    {
        return _open;
    }

    public JCheckBoxMenuItem getSaveWindowLocationItem()
    {
        return _saveWindowLocation;
    }

}
