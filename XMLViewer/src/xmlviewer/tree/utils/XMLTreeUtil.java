package xmlviewer.tree.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import xmlviewer.tree.XMLTreeExpansionState;


public class XMLTreeUtil
{
    private static final String TREE_EXPANSION_PATH = ".\\expansionStates";

    private static final String TREE_EXPANSION_FILE = TREE_EXPANSION_PATH + "\\treeExp.exp";

    /**
     * Saves the current expansion state of a JTree and returns it in an Enumeration for storing purposes.
     * 
     * @param tree
     *            Save the current expansion state of this tree
     * @return The current expansion state of the given tree as Enumeration<TreePath>
     */
    public static Enumeration<TreePath> getTreeExpansionState(JTree tree)
    {
        // test
        if (tree != null)
        {
            return tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
        }
        return null;
    }

    /**
     * Loads the given expansion state of a JTree, making it expand in the given manner.
     * 
     * @param tree
     *            The JTree to be expended
     * @param enumeration
     *            The expansion state for tree as Enumeration<TreePath>
     */
    public static void loadTreeExpansionState(JTree tree, Enumeration<TreePath> enumeration)
    {
        if (enumeration != null)
        {
            while (enumeration.hasMoreElements())
            {
                TreePath path = enumeration.nextElement();
                tree.expandPath(path);
            }
        }
    }

    public static void serializeTreeExpansionState(XMLTreeExpansionState expansionState)
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
                f.mkdir();
            }

            FileOutputStream fOut = new FileOutputStream(TREE_EXPANSION_FILE);
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

    public static XMLTreeExpansionState deserializeTreeExpansionState()
    {
        XMLTreeExpansionState result = null;

        try
        {
            File f = new File(TREE_EXPANSION_PATH);

            if (!f.exists())
            {
                f.mkdir();
            }

            f = new File(TREE_EXPANSION_FILE);

            if (!f.exists())
            {
                return null;
            }

            FileInputStream fIn = new FileInputStream(TREE_EXPANSION_FILE);
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

}
