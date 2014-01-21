package xmlviewer.tree;

import java.io.Serializable;
import java.util.Enumeration;
import javax.swing.tree.TreePath;


public class XMLTreeExpansionState implements Serializable
{
    private static final long serialVersionUID = 1L;

    public Enumeration<TreePath> _expansionState;

    public XMLTreeExpansionState(Enumeration<TreePath> expState)
    {
        _expansionState = expState;
    }

    public Enumeration<TreePath> getExpansionState()
    {
        return _expansionState;
    }
}
