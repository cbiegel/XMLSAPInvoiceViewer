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

    private final JMenuBar _menuBar;
    private final JMenu _file;
    private final JMenu _settings;
    private final JMenu _view;
    private final JMenuItem _open;
    private final JCheckBoxMenuItem _settingsSaveWindowLocation;
    private final JCheckBoxMenuItem _settingsSaveTreeState;
    private final JMenuItem _viewSwitchViews;
    private final JMenuItem _viewCollapseAll;
    private final JMenuItem _viewExpandAll;

    public XMLViewerMenu(boolean saveWindowPos, boolean saveTreeState) {
        _menuBar = new JMenuBar();
        _file = new JMenu(" File ");
        _file.setName("file");

        _settings = new JMenu(" Settings ");
        _settings.setName("settings");

        _view = new JMenu(" View ");
        _view.setName("view");

        _open = new JMenuItem("Open...");
        _open.setName("open");

        _settingsSaveWindowLocation = new JCheckBoxMenuItem("Save window location on close");
        _settingsSaveWindowLocation.setName("saveWindowLocation");
        _settingsSaveWindowLocation.setSelected(saveWindowPos);

        _settingsSaveTreeState = new JCheckBoxMenuItem("Save tree state");
        _settingsSaveTreeState.setName("saveTreeState");
        _settingsSaveTreeState.setSelected(saveTreeState);
        _settingsSaveTreeState.setEnabled(false);

        _viewSwitchViews = new JMenuItem("Switch to detailed view");
        _viewSwitchViews.setName("switchViews");
        _viewSwitchViews.setEnabled(false);

        _viewCollapseAll = new JMenuItem("Collapse all nodes");
        _viewCollapseAll.setName("collapseAll");
        _viewCollapseAll.setEnabled(false);

        _viewExpandAll = new JMenuItem("Expand all nodes");
        _viewExpandAll.setName("expandAll");
        _viewExpandAll.setEnabled(false);

        setupMenuBar();
    }

    private void setupMenuBar()
    {
        _file.setMnemonic(KeyEvent.VK_F);

        _open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        _settingsSaveWindowLocation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.ALT_MASK));
        _settingsSaveTreeState.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
        _viewSwitchViews.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        _viewCollapseAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        _viewExpandAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));

        _file.add(_open);

        _settings.add(_settingsSaveTreeState);
        _settings.add(_settingsSaveWindowLocation);

        _view.add(_viewSwitchViews);
        _view.add(_viewCollapseAll);
        _view.add(_viewExpandAll);

        _menuBar.add(_file);
        _menuBar.add(_view);
        _menuBar.add(_settings);
    }

    // getters

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

    public JMenu getViewMenu()
    {
        return _view;
    }

    public JMenuItem getOpenItem()
    {
        return _open;
    }

    public JCheckBoxMenuItem getSettingsSaveWindowLocationItem()
    {
        return _settingsSaveWindowLocation;
    }

    public JCheckBoxMenuItem getSettingsSaveTreeStateItem()
    {
        return _settingsSaveTreeState;
    }

    public JMenuItem getViewSwitchViewsItem()
    {
        return _viewSwitchViews;
    }

    public JMenuItem getViewCollapseAllItem()
    {
        return _viewCollapseAll;
    }

    public JMenuItem getViewExpandAllItem()
    {
        return _viewExpandAll;
    }

}
