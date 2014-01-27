package tests.tree;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import xmlviewer.tree.XMLTreeExpansionState;


public class XMLTreeExpansionStateTest {

    private XMLTreeExpansionState _testState;

    @Before
    public void setUp()
    {
        _testState = new XMLTreeExpansionState(",0");
    }

    @Test
    public void testGetExpansionState()
    {
        assertEquals(",0", _testState.getExpansionState());
    }

}
