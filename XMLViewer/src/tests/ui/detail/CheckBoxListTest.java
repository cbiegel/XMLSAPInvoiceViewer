package tests.ui.detail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import xmlviewer.ui.CustomCheckBox;
import xmlviewer.ui.detail.CheckBoxList;


public class CheckBoxListTest
{
    private CheckBoxList _list;

    @Before
    public void setUp()
            throws Exception
    {
        _list = new CheckBoxList();
    }

    @Test
    public void testAddCheckBox()
    {
        assertNull(_list.getCheckBoxAtPosition(0));
        assertEquals(0, _list.getListSize());
        CustomCheckBox cb = new CustomCheckBox("cb");
        _list.addCheckBox(cb);
        assertEquals(cb, _list.getCheckBoxAtPosition(0));
        assertEquals(1, _list.getListSize());
        assertTrue(_list.containsCheckBox(cb));

        // CheckBoxList only adds check boxes if they are not in the list already (much like a set)
        _list.addCheckBox(cb);
        assertEquals(1, _list.getListSize());

        CustomCheckBox cb2 = new CustomCheckBox("cb2");
        _list.addCheckBox(cb2);
        assertEquals(2, _list.getListSize());
        assertEquals(cb, _list.getCheckBoxAtPosition(0));
        assertEquals(cb2, _list.getCheckBoxAtPosition(1));
        assertTrue(_list.containsCheckBox(cb2));
    }

    @Test
    public void testRemoveCheckBox()
    {
        CustomCheckBox testCheckBox = null;

        for (int c = 0; c < 10; c++)
        {
            testCheckBox = new CustomCheckBox("cb" + c);
            _list.addCheckBox(testCheckBox);
        }
        assertEquals(10, _list.getListSize());
        assertTrue(_list.containsCheckBox(testCheckBox));
        _list.removeCheckBox(testCheckBox);
        assertEquals(9, _list.getListSize());
        assertFalse(_list.containsCheckBox(testCheckBox));
    }

    @Test
    public void testDisplayCheckBoxComponents()
    {
        Map<CustomCheckBox, List<CustomCheckBox>> attributeMap = new HashMap<CustomCheckBox, List<CustomCheckBox>>();
        List<CustomCheckBox> attributeList = new ArrayList<CustomCheckBox>();
        CustomCheckBox parentHeader = new CustomCheckBox("parent");
        CustomCheckBox attr1 = new CustomCheckBox("attribute1");
        CustomCheckBox attr2 = new CustomCheckBox("attribute2");
        CustomCheckBox attr3 = new CustomCheckBox("attribute3");
        CustomCheckBox attr4 = new CustomCheckBox("attribute4");
        CustomCheckBox attr5 = new CustomCheckBox("attribute5");

        attributeList.add(attr1);
        attributeList.add(attr2);
        attributeList.add(attr3);
        attributeList.add(attr4);
        attributeList.add(attr5);
        attributeMap.put(parentHeader, attributeList);

        _list.displayCheckBoxComponents(attributeMap);
        CustomCheckBox[] allCheckBoxes = _list.getAllCheckBoxes();

        assertEquals(7, allCheckBoxes.length);
        assertEquals(0, _list.getSelectedAttributes(parentHeader).length);

        allCheckBoxes[1].setSelected(true);
        allCheckBoxes[3].setSelected(true);
        allCheckBoxes[4].setSelected(true);

        assertEquals(3, _list.getSelectedAttributes(parentHeader).length);
    }
}
