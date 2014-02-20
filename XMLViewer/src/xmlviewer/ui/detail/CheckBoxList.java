package xmlviewer.ui.detail;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class CheckBoxList extends JList
{
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public CheckBoxList()
    {
        setCellRenderer(new CellRenderer());

        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                int index = locationToIndex(e.getPoint());

                if (index != -1) {
                    JCheckBox checkbox = (JCheckBox)
                        getModel().getElementAt(index);
                    checkbox.setSelected(
                            !checkbox.isSelected());
                    repaint();
                }
            }
        });

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setFixedCellHeight(20);
    }

    public void makeUnionAndDisplayData(List<Set<JCheckBox>> attributeSets)
    {
        if (attributeSets.size() == 0)
        {
            setListData(new JCheckBox[0]);
        }
        else
        {
            Set<JCheckBox> firstSet = attributeSets.get(0);
            for (Set<JCheckBox> attributeSet : attributeSets)
            {
                firstSet.addAll(attributeSet);
            }

            JCheckBox[] checkBoxArray = firstSet.toArray(new JCheckBox[firstSet.size()]);
            checkBoxArray = sortCheckBoxesAlphabetically(checkBoxArray);

            setListData(checkBoxArray);
        }
    }

    public void displayCheckBoxComponents(Map<JCheckBox, List<JCheckBox>> childrenAttributesMap)
    {
        List<JCheckBox> display = new ArrayList<JCheckBox>();

        for (JCheckBox cb : childrenAttributesMap.keySet())
        {
            List<JCheckBox> component = getSingleCheckBoxComponent(cb, childrenAttributesMap.get(cb));
            for (JCheckBox jCheckBox : component)
            {
                display.add(jCheckBox);
            }
        }
        JCheckBox[] checkBoxArray = display.toArray(new JCheckBox[display.size()]);
        setListData(checkBoxArray);
    }

    private List<JCheckBox> getSingleCheckBoxComponent(JCheckBox header, List<JCheckBox> attributeList)
    {
        List<JCheckBox> result = new ArrayList<JCheckBox>();
        result.add(getHeaderBox(header));

        JCheckBox[] attributes = attributeList.toArray(new JCheckBox[attributeList.size()]);
        attributes = sortCheckBoxesAlphabetically(attributes);

        for (JCheckBox cb : attributes)
        {
            result.add(cb);
        }

        result.add(getInvisibleBox());

        return result;
    }

    private JCheckBox[] sortCheckBoxesAlphabetically(JCheckBox[] arr)
    {
        JCheckBox[] result = new JCheckBox[arr.length];
        String[] names = new String[arr.length];

        for (int c = 0; c < arr.length; c++)
        {
            names[c] = arr[c].getText();
        }

        Arrays.sort(names);

        for (int c = 0; c < names.length; c++)
        {
            JCheckBox cb = new JCheckBox(names[c]);
            result[c] = cb;
        }

        return result;
    }

    public void addCheckBox(JCheckBox checkBox)
    {
        JCheckBox[] currentCheckBoxes = getAllCheckBoxes();
        JCheckBox[] newCheckBoxes = new JCheckBox[currentCheckBoxes.length + 1];
        int containsPos = containsCheckAtPositionBox(currentCheckBoxes, checkBox);

        // only add checkBox if it is not in the current list already
        if (containsPos == -1)
        {
            for (int c = 0; c < currentCheckBoxes.length; c++)
            {
                newCheckBoxes[c] = currentCheckBoxes[c];
            }

            newCheckBoxes[newCheckBoxes.length - 1] = checkBox;
            setListData(newCheckBoxes);
        }
    }

    public void removeCheckBox(JCheckBox checkBox)
    {
        JCheckBox[] currentCheckBoxes = getAllCheckBoxes();
        int containsPosition = containsCheckAtPositionBox(currentCheckBoxes, checkBox);

        if (containsPosition != -1)
        {
            JCheckBox[] newData = new JCheckBox[currentCheckBoxes.length - 1];

            for (int c = 0; c < containsPosition; c++)
            {
                newData[c] = currentCheckBoxes[c];
            }
            for (int c = containsPosition; c < newData.length; c++)
            {
                newData[c] = currentCheckBoxes[c + 1];
            }
            setListData(newData);
        }
    }

    /**
     * Checks if checkBoxSet contains checkBox. If it does contain checkBox, returns the position in the array of
     * checkBox.
     * If it does not contain checkBox, returns -1.
     */
    private int containsCheckAtPositionBox(JCheckBox[] checkBoxSet, JCheckBox checkBox)
    {
        // check if currentCheckBoxes contains checkBox
        for (int c = 0; c < checkBoxSet.length; c++)
        {
            if (checkBoxSet[c].getText().equals(checkBox.getText()))
            {
                return c;
            }
        }

        return -1;
    }

    public JCheckBox[] getAllCheckBoxes()
    {
        ListModel model = getModel();
        JCheckBox[] result = new JCheckBox[model.getSize()];

        // prevent an ArrayIndexOutOfBoundsException for the model
        if (model.getSize() == 0)
        {
            return result;
        }

        // model is empty (holds no objects)
        if (model.getElementAt(0).toString().equals("No Data Model"))
        {
            setListData(new Object[] {});
        }
        else
        {
            for (int c = 0; c < model.getSize(); c++)
            {
                result[c] = (JCheckBox) model.getElementAt(c);
            }
        }

        return result;
    }

    public JCheckBox[] getSelectedCheckBoxes()
    {
        JCheckBox[] allCheckBoxes = getAllCheckBoxes();
        List<JCheckBox> selectedCheckBoxes = new ArrayList<JCheckBox>();

        for (JCheckBox cb : allCheckBoxes)
        {
            if (cb.isSelected())
            {
                selectedCheckBoxes.add(cb);
            }
        }

        return selectedCheckBoxes.toArray(new JCheckBox[selectedCheckBoxes.size()]);
    }

    private JCheckBox getHeaderBox(JCheckBox cb)
    {
        JCheckBox result = new JCheckBox(cb.getText())
        {
            @Override
            public boolean isSelected()
            {
                return false;
            }
        };
        Icon invis = new ImageIcon();
        result.setIcon(invis);
        result.setSelectedIcon(invis);
        return result;
    }

    private JCheckBox getInvisibleBox()
    {
        JCheckBox result = new JCheckBox("")
        {
            @Override
            public boolean isSelected()
            {
                return false;
            }
        };
        Icon invis = new ImageIcon();
        result.setIcon(invis);
        result.setSelectedIcon(invis);
        return result;
    }

    @Override
    public int locationToIndex(Point location)
    {
        int index = super.locationToIndex(location);
        if (index != -1 && !getCellBounds(index, index).contains(location))
        {
            return -1;
        }
        else
        {
            return index;
        }
    }

    protected class CellRenderer implements ListCellRenderer
    {
        public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
        {
            if (value == null)
            {
                System.out.println("List: " + list + "\nIndex: " + index + "\nisSelected: " + isSelected + "\ncellHasFocus: "
                    + cellHasFocus);
                return null;
            }

            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(isSelected ?
                getBackground() : getBackground());
            checkbox.setForeground(isSelected ?
                getForeground() : getForeground());
            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(false);
            checkbox.setBorder(isSelected ?
                UIManager.getBorder(
                        "List.focusCellHighlightBorder") : noFocusBorder);
            return checkbox;
        }
    }
}