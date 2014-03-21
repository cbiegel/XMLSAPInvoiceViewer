package xmlviewer.tree.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import org.w3c.dom.Node;
import xmlviewer.tree.XMLTreeExpansionState;


public class XMLTreeUtil
{
    private static final String APPDATA_PATH = System.getenv("APPDATA");

    private static final String TREE_EXPANSION_PATH = APPDATA_PATH + "\\XMLViewer\\expansionStates";

    private static final String TREE_EXPANSION_FILE = TREE_EXPANSION_PATH + "\\treeExp-";

    private static final String TREE_EXPANSION_FILE_SUFFIX = ".exp";

    /**
     * @return true, if path1 is a descendant of path2. Else, returns false.
     */
    public static boolean isDescendant(TreePath path1, TreePath path2) {
        int count1 = path1.getPathCount();
        int count2 = path2.getPathCount();
        if (count1 <= count2)
            return false;
        while (count1 != count2) {
            path1 = path1.getParentPath();
            count1--;
        }
        return path1.equals(path2);
    }

    /**
     * Saves the current expansion state of a JTree and returns it in an Enumeration for storing purposes. Example: A
     * JTree that has all its branches collapsed will return a TreeExpansionState of ",0".
     * 
     * @param tree
     *            Save the current expansion state of this tree
     * @return The current expansion state of the given tree as Enumeration<TreePath>
     */
    public static String getTreeExpansionState(JTree tree, int row)
    {
        TreePath rowPath = tree.getPathForRow(row);
        StringBuilder sb = new StringBuilder();
        int rowCount = tree.getRowCount();

        for (int i = row; i < rowCount; i++)
        {
            TreePath path = tree.getPathForRow(i);

            if (i == row || isDescendant(path, rowPath))
            {
                if (tree.isExpanded(path))
                    sb.append("," + String.valueOf(i - row));
            }
            else
            {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Loads the given expansion state of a JTree, making it expand in the given manner.
     * 
     * @param tree
     *            The JTree to be expended
     * @param enumeration
     *            The expansion state for tree as Enumeration<TreePath>
     */
    public static void loadTreeExpansionState(JTree tree, String expansionState, int row)
    {
        // if a tree is opened for the first time, its expansionState is null
        if (expansionState == null)
        {
            return;
        }

        StringTokenizer stok = new StringTokenizer(expansionState, ",");
        while (stok.hasMoreTokens()) {
            int token = row + Integer.parseInt(stok.nextToken());
            tree.expandRow(token);
        }
    }

    public static void expandAllNodes(JTree tree)
    {
        int row = 0;

        while (row < tree.getRowCount())
        {
            tree.expandRow(row);
            ++row;
        }
    }

    public static void collapseAllNodes(JTree tree)
    {
        int row = tree.getRowCount() - 1;

        while (row > 0)
        {
            tree.collapseRow(row);
            --row;
        }
    }

    public static void serializeTreeExpansionState(XMLTreeExpansionState expansionState, int hash)
    {
        // do nothing if expansionState is null
        if (expansionState.getExpansionState() == null)
        {
            return;
        }

        try
        {
            File f = new File(TREE_EXPANSION_PATH);

            if (!f.exists())
            {
                f.mkdirs();
            }
            if (!f.exists())
            {
                JOptionPane.showMessageDialog(null, "Warning: No write permissions for: \n" + f.getAbsolutePath(),
                    "Error saving tree state", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String outputFilePath = TREE_EXPANSION_FILE + hash + TREE_EXPANSION_FILE_SUFFIX;

            FileOutputStream fOut = new FileOutputStream(outputFilePath);
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);

            oOut.writeObject(expansionState);

            oOut.close();
            fOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static XMLTreeExpansionState deserializeTreeExpansionState(int hash)
    {
        XMLTreeExpansionState result = null;

        String inputFilePath = TREE_EXPANSION_FILE + hash + TREE_EXPANSION_FILE_SUFFIX;

        try
        {
            File f = new File(TREE_EXPANSION_PATH);

            if (!f.exists())
            {
                f.mkdir();
            }

            f = new File(inputFilePath);

            if (!f.exists())
            {
                return null;
            }

            FileInputStream fIn = new FileInputStream(inputFilePath);
            ObjectInputStream oIn = new ObjectInputStream(fIn);

            result = (XMLTreeExpansionState) oIn.readObject();

            oIn.close();
            fIn.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Determines whether the given JTree represents a TsSapInvoiceExternal from the COAST system
     * 
     * @return True, if tree is a TsSapInvoiceExternal (converted from the corresponding XML file). False, if it is not.
     */
    public static boolean isTsSapInvoiceListExternal(JTree tree)
    {
        Node root = (Node) tree.getModel().getRoot();
        return root.getNodeName().equals("com.scandlines.coast.common.payment.TsSapInvoiceListExternal");
    }
}
