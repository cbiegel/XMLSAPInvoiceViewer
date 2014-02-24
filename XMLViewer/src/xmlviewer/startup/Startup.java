package xmlviewer.startup;

import javax.swing.UIManager;
import xmlviewer.ui.tools.MainWindowTool;


public class Startup {

    /**
     * Main method. Creates an instance of MainWindowTool (which subsequently creates an instance of MainWindow)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            @SuppressWarnings("unused")
            MainWindowTool start = new MainWindowTool();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
