package xmlviewer.startup;

import javax.swing.UIManager;
import xmlviewer.tree.util.DetailedViewUtil;
import xmlviewer.ui.tools.MainWindowTool;


public class Startup {

    /**
     * Main method. Creates an instance of MainWindowTool (which subsequently creates an instance of MainWindow)
     */
    public static void main(String[] args) {

        String[][] test = { {"foo", "bar"}, {"fooAmount", "20.0"}, {"fooCurrency", "EUR"}, {"someAttr", "someValue"}};
        String[][] test2 = DetailedViewUtil.getCompactAttributes(test);
        for (int c = 0; c < test2.length; c++)
        {
            System.out.printf("%s %s\n", test2[c][0], test2[c][1]);
        }

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
