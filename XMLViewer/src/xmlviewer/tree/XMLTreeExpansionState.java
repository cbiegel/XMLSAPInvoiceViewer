package xmlviewer.tree;

import java.io.Serializable;


public class XMLTreeExpansionState implements Serializable
{
    private static final long serialVersionUID = 1L;

    public String _expansionState;

    public XMLTreeExpansionState(String expState)
    {
        _expansionState = expState;
    }

    public String getExpansionState()
    {
        return _expansionState;
    }
}
