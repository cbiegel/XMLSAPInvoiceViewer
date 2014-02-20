package xmlviewer.ui.main;

import java.awt.Component;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * This class is a custom modification of a JFileChooser fit to XML files.
 * 
 * @author cbiegel
 */
public class XMLViewerFileChooser {

    private final JFileChooser _fileChooser;
    private static String _lastFilePath = "";
    private static boolean _filePathChanged = false;
    private static final String ICON_FILE_PATH = ".\\Resources\\XMLViewerIcon_16_16.png";

    public XMLViewerFileChooser(Component parentFrame) {
        _fileChooser = new JFileChooser("Open an XML file");
        setupFileChooser(parentFrame);
    }

    private void setupFileChooser(Component parentFrame)
    {
        _fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        _fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        final File startDir = new File(System.getProperty("user.home"));
        _fileChooser.setCurrentDirectory(startDir);

        FileFilter xmlFilter = new FileNameExtensionFilter("XML file (*.xml)", "xml");
        _fileChooser.setFileFilter(xmlFilter);
        _fileChooser.addChoosableFileFilter(xmlFilter);
        _fileChooser.setAcceptAllFileFilterUsed(false);

        // JFileChooser inherits the icon of the parent in showOpenDialog
        ImageIcon icon = new ImageIcon(ICON_FILE_PATH);
        JFrame iconFrame = new JFrame();
        iconFrame.setIconImage(icon.getImage());
        final int result = _fileChooser.showOpenDialog(parentFrame);

        _fileChooser.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            File chosenFile = _fileChooser.getSelectedFile();

            // if a new file was selected, set to true
            _filePathChanged = _lastFilePath.equals(chosenFile.getPath()) ? false : true;

            _lastFilePath = chosenFile.getPath();

        }
        _fileChooser.setVisible(false);
    }

    public JFileChooser getCustomFileChooser()
    {
        return _fileChooser;
    }

    public String getLastFilePath()
    {
        return _lastFilePath;
    }

    public boolean hasPathChanged()
    {
        return _filePathChanged;
    }
}
