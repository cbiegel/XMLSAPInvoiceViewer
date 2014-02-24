package xmlviewer.ui.detail;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import xmlviewer.ui.CustomCheckBox;


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
                    CustomCheckBox checkbox = (CustomCheckBox)
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

    /**
     * Sets the list data according to the given data.
     * 
     * @param childrenAttributesMap
     *            A map which maps parents to their list of attributes
     */
    public void displayCheckBoxComponents(Map<CustomCheckBox, List<CustomCheckBox>> childrenAttributesMap)
    {
        List<CustomCheckBox> display = new ArrayList<CustomCheckBox>();

        for (CustomCheckBox cb : childrenAttributesMap.keySet())
        {
            List<CustomCheckBox> component = getSingleCheckBoxComponent(cb, childrenAttributesMap.get(cb));
            for (CustomCheckBox jCheckBox : component)
            {
                display.add(jCheckBox);
            }
        }
        CustomCheckBox[] checkBoxArray = display.toArray(new CustomCheckBox[display.size()]);
        setListData(checkBoxArray);
    }

    private List<CustomCheckBox> getSingleCheckBoxComponent(CustomCheckBox header, List<CustomCheckBox> attributeList)
    {
        List<CustomCheckBox> result = new ArrayList<CustomCheckBox>();
        result.add(getHeaderBox(header));

        CustomCheckBox[] attributes = attributeList.toArray(new CustomCheckBox[attributeList.size()]);

        for (CustomCheckBox cb : attributes)
        {
            result.add(cb);
        }

        result.add(getInvisibleBox());

        return result;
    }

    /**
     * Adds a check box to the list and refreshes its content in the display. A check box can only be added to the list
     * if
     * it does not contain it already
     * 
     * @param checkBox
     *            The check box to be added to the list
     */
    public void addCheckBox(CustomCheckBox checkBox)
    {
        CustomCheckBox[] currentCheckBoxes = getAllCheckBoxes();
        CustomCheckBox[] newCheckBoxes = new CustomCheckBox[currentCheckBoxes.length + 1];
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

    /**
     * Removes a check box from the list (if it exists)
     * 
     * @param checkBox
     *            The check box to be removed from the list
     */
    public void removeCheckBox(CustomCheckBox checkBox)
    {
        CustomCheckBox[] currentCheckBoxes = getAllCheckBoxes();
        int containsPosition = containsCheckAtPositionBox(currentCheckBoxes, checkBox);

        if (containsPosition != -1)
        {
            CustomCheckBox[] newData = new CustomCheckBox[currentCheckBoxes.length - 1];

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
    private int containsCheckAtPositionBox(CustomCheckBox[] checkBoxSet, CustomCheckBox checkBox)
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

    /**
     * Collects all check boxes in the list and bundles them in an array
     * 
     * @return A CustomCheckBox[] that contains every check box in this list
     */
    public CustomCheckBox[] getAllCheckBoxes()
    {
        ListModel model = getModel();
        CustomCheckBox[] result = new CustomCheckBox[model.getSize()];

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
                result[c] = (CustomCheckBox) model.getElementAt(c);
            }
        }

        return result;
    }

    /**
     * Collects every selected attribute (their check box is marked as selected) of a parent to an array
     * 
     * @return Every selected attribute of parent in a CustomCheckBox[]
     */
    public CustomCheckBox[] getSelectedAttributes(CustomCheckBox parent)
    {
        CustomCheckBox[] allCheckBoxes = getAllCheckBoxes();
        List<CustomCheckBox> selectedCheckBoxes = new ArrayList<CustomCheckBox>();
        int headerPosition = -1;
        int trailerPosition = -1;

        // find the parent header
        for (int c = 0; c < allCheckBoxes.length; c++)
        {
            if (allCheckBoxes[c].isHeader())
            {
                if (allCheckBoxes[c].getText().equals(parent.getText()))
                {
                    headerPosition = c;
                    break;
                }
            }
        }

        // find the corresponding trailer
        for (int c = headerPosition; c < allCheckBoxes.length; c++)
        {
            if (allCheckBoxes[c].isTrailer())
            {
                trailerPosition = c;
                break;
            }
        }

        // get all the selected check boxes in the interval (headerPosition, trailerPosition)
        for (int c = headerPosition + 1; c < trailerPosition; c++)
        {
            if (allCheckBoxes[c].isSelected())
            {
                selectedCheckBoxes.add(allCheckBoxes[c]);
            }
        }

        return selectedCheckBoxes.toArray(new CustomCheckBox[selectedCheckBoxes.size()]);
    }

    private CustomCheckBox getHeaderBox(CustomCheckBox cb)
    {
        CustomCheckBox result = new CustomCheckBox(cb.getText())
        {
            @Override
            public boolean isSelected()
            {
                return false;
            }

            @Override
            public boolean isHeader()
            {
                return true;
            }
        };
        Icon invis = new ImageIcon();
        result.setIcon(invis);
        result.setSelectedIcon(invis);
        return result;
    }

    private CustomCheckBox getInvisibleBox()
    {
        CustomCheckBox result = new CustomCheckBox("")
        {
            @Override
            public boolean isSelected()
            {
                return false;
            }

            @Override
            public boolean isTrailer()
            {
                return true;
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
            CustomCheckBox checkbox = (CustomCheckBox) value;
            checkbox.setBackground(getBackground());
            checkbox.setForeground(getForeground());
            checkbox.setEnabled(checkbox.isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(false);
            checkbox.setBorder(isSelected ?
                UIManager.getBorder(
                        "List.focusCellHighlightBorder") : noFocusBorder);
            repaint();
            return checkbox;
        }
    }
}