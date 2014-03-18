package xmlviewer.ui.main;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
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
    private final JMenuItem _fileOpen;
    private final JMenuItem _fileExit;
    private final JCheckBoxMenuItem _settingsSaveWindowLocation;
    private final JCheckBoxMenuItem _settingsSaveTreeState;
    private final JMenuItem _viewSwitchViews;
    private final JMenuItem _viewCollapseAll;
    private final JMenuItem _viewExpandAll;
    private final JCheckBoxMenuItem _viewApplyFilter;
    private final JCheckBoxMenuItem _viewShowCompact;

    public XMLViewerMenu(boolean saveWindowPos, boolean saveTreeState)
    {
        _menuBar = new JMenuBar();
        _file = new JMenu(" File ");
        _file.setName("file");

        _settings = new JMenu(" Settings ");
        _settings.setName("settings");

        _view = new JMenu(" View ");
        _view.setName("view");

        _fileOpen = new JMenuItem("Open...");
        _fileOpen.setName("open");

        _fileExit = new JMenuItem("Exit");
        _fileExit.setName("exit");

        _settingsSaveWindowLocation = new JCheckBoxMenuItem("Save window location on close");
        _settingsSaveWindowLocation.setName("saveWindowLocation");
        _settingsSaveWindowLocation.setSelected(saveWindowPos);

        _settingsSaveTreeState = new JCheckBoxMenuItem("Save tree state on close");
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

        _viewApplyFilter = new JCheckBoxMenuItem("Apply filter");
        _viewApplyFilter.setName("applyFilter");
        _viewApplyFilter.setSelected(false);
        _viewApplyFilter.setEnabled(false);

        _viewShowCompact = new JCheckBoxMenuItem("Show compact attributes");
        _viewShowCompact.setName("showCompact");
        _viewShowCompact.setSelected(false);
        _viewShowCompact.setEnabled(false);

        setupMenuBar();
    }

    private void setupMenuBar()
    {
        _file.setMnemonic(KeyEvent.VK_F);

        _fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        _fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        _settingsSaveWindowLocation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.ALT_MASK));
        _settingsSaveTreeState.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
        _viewSwitchViews.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        _viewCollapseAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        _viewExpandAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        _viewApplyFilter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
        _viewShowCompact.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));

        _file.add(_fileOpen);
        _file.add(new JSeparator());
        _file.add(_fileExit);

        _settings.add(_settingsSaveTreeState);
        _settings.add(_settingsSaveWindowLocation);

        _viewSwitchViews.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
            (getClass().getResource(MainWindow.ICON_DETAILED_VIEW_PATH)))));
        _viewCollapseAll.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
            (getClass().getResource(MainWindow.ICON_COLLAPSE_PATH)))));
        _viewExpandAll.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
            (getClass().getResource(MainWindow.ICON_EXPAND_PATH)))));

        _view.add(_viewSwitchViews);
        _view.add(new JSeparator());
        _view.add(_viewCollapseAll);
        _view.add(_viewExpandAll);
        _view.add(new JSeparator());
        _view.add(_viewApplyFilter);
        _view.add(_viewShowCompact);

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
        return _fileOpen;
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

    public JCheckBoxMenuItem getViewApplyFilterItem()
    {
        return _viewApplyFilter;
    }

    public JCheckBoxMenuItem getViewShowCompactItem()
    {
        return _viewShowCompact;
    }

    public JMenuItem getExitItem()
    {
        return _fileExit;
    }

}
