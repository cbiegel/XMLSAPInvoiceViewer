package xmlviewer.startup;

import javax.swing.UIManager;
import xmlviewer.ui.tools.MainWindowTool;


public class Startup {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // TODO: remove comments

            // InputStream ist = new FileInputStream(new File(".\\TestResources\\deleteMe.xml"));
            // InputSource is = new InputSource(ist);
            // XMLTreeModel model = new XMLTreeModel(is);
            // JTree tree = new JTree(model);
            // Node root = (Node) tree.getModel().getRoot();
            // NodeList children = root.getChildNodes();
            // Node sapInvoiceExternals = children.item(3);
            // NodeList sapInvoiceElements = sapInvoiceExternals.getChildNodes();
            // Node elem1 = sapInvoiceElements.item(0);
            // String[] elemList = DetailedViewUtil.getSubElementsListFromTree(elem1);
            // // for (String string : elemList)
            // // {
            // // System.out.println(string);
            // // }
            // // System.out.println("______________________________________________________");
            // Map<String, Node> nodeMap = DetailedViewUtil.getNodeMap();
            // Node mCashBookHeader = nodeMap.get("|_ mCashBookHeader");
            // String[][] tableData = DetailedViewUtil.getDetailsForSubElement(mCashBookHeader);
            // for (String[] strings : tableData) {
            // for (String string : strings) {
            // System.out.println(string);
            // }
            // }

            @SuppressWarnings("unused")
            MainWindowTool start = new MainWindowTool();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
