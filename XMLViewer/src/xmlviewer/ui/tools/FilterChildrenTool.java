package xmlviewer.ui.tools;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xmlviewer.tree.util.DetailedViewUtil;
import xmlviewer.ui.CustomCheckBox;
import xmlviewer.ui.detail.FilterChildren;


/**
 * @author cbiegel
 *         This class serves as the tool component for the FilterChildren component
 */
public class FilterChildrenTool
{
    private FilterChildren _ui;
    private Node _parentNode;
    private int _parentNodeIndex;
    private Map<CustomCheckBox, List<CustomCheckBox>> _childrenAttributesMap;
    private Map<String, String[]> _result;

    public FilterChildrenTool(Node parentNode, int parentNodeIndex, JFrame owner)
    {
        _parentNode = parentNode;
        _parentNodeIndex = parentNodeIndex;
        _childrenAttributesMap = new HashMap<CustomCheckBox, List<CustomCheckBox>>();
        _ui = new FilterChildren(owner);
        initializeUI();
        setupListeners();
    }

    private void initializeUI()
    {
        if (_parentNode != null)
        {
            _ui.getUIParentNameLabel().setText(_parentNode.getNodeName());
        }

        String[] children = DetailedViewUtil.getSubElementsListFromTree(_parentNode, false, false);
        String[] trimmedData = new String[children.length];

        for (int c = 0; c < children.length; c++)
        {
            String trimmedString = DetailedViewUtil.stripStringToLettersAndNumbers(children[c]);
            trimmedData[c] = trimmedString;
        }

        List<String> childrenList = getChildrenList(trimmedData);

        for (String s : childrenList)
        {
            CustomCheckBox checkBox = new CustomCheckBox(s);
            String[] childNodes = DetailedViewUtil.getSubElementsListFromTree(_parentNode, false, false);
            String[] directAttributes = getDirectAttributesSetForElement(childNodes, checkBox.getText());
            String[] indirectAttributes = getIndirectAttributesSetForElement(childNodes, checkBox.getText());
            Set<CustomCheckBox> checkBoxAttributesSet = new HashSet<CustomCheckBox>();

            // add direct attributes
            for (int c = 0; c < directAttributes.length; c++)
            {
                CustomCheckBox cb = new CustomCheckBox(directAttributes[c]);
                checkBoxAttributesSet.add(cb);
            }

            // add indirect attributes
            for (int c = 0; c < indirectAttributes.length; c++)
            {
                CustomCheckBox cb = new CustomCheckBox(indirectAttributes[c]);
                checkBoxAttributesSet.add(cb);
            }

            List<CustomCheckBox> checkBoxAttributesList = new ArrayList<CustomCheckBox>();
            checkBoxAttributesList.addAll(checkBoxAttributesSet);

            // sort alphabetically
            checkBoxAttributesList = sortCheckBoxesAlphabetically(checkBoxAttributesList);

            _childrenAttributesMap.put(checkBox, checkBoxAttributesList);

            _ui.getUIChildrenCheckBoxList().addCheckBox(checkBox);
        }
    }

    private void setupListeners()
    {
        JButton okButton = _ui.getUIOkButton();
        JButton cancelButton = _ui.getUICancelButton();
        JButton childrenSelectAll = _ui.getUISelectAllChildrenButton();
        JButton childrenDeselectAll = _ui.getUIDeselectAllChildrenButton();
        JButton attrSelectAll = _ui.getUISelectAllAttributesButton();
        JButton attrDeselectAll = _ui.getUIDeselectAllAttributesButton();

        addOKButtonListener(okButton);

        for (CustomCheckBox checkBox : _ui.getUIChildrenCheckBoxList().getAllCheckBoxes())
        {
            addChildrenCheckBoxListener(checkBox);
        }

        addCancelButtonListener(cancelButton);
        addSelectAllChildrenCheckBoxesListener(childrenSelectAll);
        addDeselectAllChildrenCheckBoxListener(childrenDeselectAll);
        addSelectAllAttributesCheckBoxListener(attrSelectAll);
        addDeselectAllAttributesCheckBoxListener(attrDeselectAll);

    }

    private void addOKButtonListener(JButton button)
    {
        button.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                Map<String, String[]> result = new HashMap<String, String[]>();
                for (CustomCheckBox cb : _childrenAttributesMap.keySet())
                {
                    if (cb.isSelected())
                    {
                        CustomCheckBox[] selectedAttributes = _ui.getUIAttributesCheckBoxList().getSelectedAttributes(cb);
                        String parentName = cb.getText();
                        String[] attributes = new String[selectedAttributes.length];
                        for (int c = 0; c < selectedAttributes.length; c++)
                        {
                            attributes[c] = selectedAttributes[c].getText();
                        }
                        result.put(parentName, attributes);
                    }
                }

                _result = result;
                _ui.dispose();
            }
        });
    }

    private void addCancelButtonListener(JButton button)
    {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                _result = null;
                _ui.dispose();
            }
        });
    }

    private void addChildrenCheckBoxListener(final CustomCheckBox checkBox)
    {
        checkBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event)
            {
                List<CustomCheckBox> selectedChildren = new ArrayList<CustomCheckBox>();
                Map<CustomCheckBox, List<CustomCheckBox>> selectedChildrenMap =
                    new HashMap<CustomCheckBox, List<CustomCheckBox>>();

                for (CustomCheckBox cb : _childrenAttributesMap.keySet())
                {
                    if (cb.isSelected())
                    {
                        selectedChildren.add(cb);
                    }
                }

                for (CustomCheckBox cb : selectedChildren)
                {
                    List<CustomCheckBox> attributes = new ArrayList<CustomCheckBox>();
                    attributes.addAll(_childrenAttributesMap.get(cb));
                    selectedChildrenMap.put(cb, attributes);
                }

                _ui.getUIAttributesCheckBoxList().displayCheckBoxComponents(selectedChildrenMap);
            }
        });
    }

    private void addSelectAllChildrenCheckBoxesListener(JButton button)
    {
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                CustomCheckBox[] allCheckBoxes = _ui.getUIChildrenCheckBoxList().getAllCheckBoxes();
                for (CustomCheckBox cb : allCheckBoxes)
                {
                    cb.setSelected(true);
                }
            }
        });
    }

    private void addDeselectAllChildrenCheckBoxListener(JButton button)
    {
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                CustomCheckBox[] allCheckBoxes = _ui.getUIChildrenCheckBoxList().getAllCheckBoxes();
                for (CustomCheckBox cb : allCheckBoxes)
                {
                    cb.setSelected(false);
                }
            }
        });
    }

    private void addSelectAllAttributesCheckBoxListener(JButton button)
    {
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                CustomCheckBox[] allCheckBoxes = _ui.getUIAttributesCheckBoxList().getAllCheckBoxes();
                for (CustomCheckBox cb : allCheckBoxes)
                {
                    cb.setSelected(true);
                }
            }
        });
    }

    private void addDeselectAllAttributesCheckBoxListener(JButton button)
    {
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                CustomCheckBox[] allCheckBoxes = _ui.getUIAttributesCheckBoxList().getAllCheckBoxes();
                for (CustomCheckBox cb : allCheckBoxes)
                {
                    cb.setSelected(false);
                }
            }
        });
    }

    private List<String> getChildrenList(String[] children)
    {
        List<String> result = new ArrayList<String>();

        for (String s : children)
        {
            if (!s.equals("el") && !result.contains(s))
            {
                result.add(s);
            }
        }

        return result;
    }

    private String[] getDirectAttributesSetForElement(String[] childNodes, String elementName)
    {
        Map<Integer, Node> nodeMap = DetailedViewUtil.getNodeMap();
        String[] directAttributes = null;

        for (int c = 0; c < childNodes.length; c++)
        {
            String formattedChildName =
                DetailedViewUtil.formatChildName(DetailedViewUtil.stripStringToLettersAndNumbers(childNodes[c]));
            String formattedElementName = DetailedViewUtil.formatChildName(elementName);

            if (formattedChildName.equals(formattedElementName))
            {
                Node element = nodeMap.get(_parentNodeIndex + c + 1);
                String[][] details = DetailedViewUtil.getDetailsForSubElement(element);
                directAttributes = new String[details.length];

                for (int i = 0; i < details.length; i++)
                {
                    directAttributes[i] = details[i][0];
                }
            }
        }

        return directAttributes;
    }

    private String[] getIndirectAttributesSetForElement(String[] childNodes, String elementName)
    {
        Map<Integer, Node> nodeMap = DetailedViewUtil.getNodeMap();
        List<String> indirectAttributes = new ArrayList<String>();

        for (int c = 0; c < childNodes.length; c++)
        {
            String formattedChildName =
                DetailedViewUtil.formatChildName(DetailedViewUtil.stripStringToLettersAndNumbers(childNodes[c]));
            String formattedElementName = DetailedViewUtil.formatChildName(elementName);

            if (formattedChildName.equals(formattedElementName))
            {
                Node element = nodeMap.get(_parentNodeIndex + c + 1);
                NodeList children = element.getChildNodes();

                if (children.getLength() > 0)
                {
                    for (int i = 0; i < children.getLength(); i++)
                    {
                        Node child = children.item(i);
                        String[][] details = DetailedViewUtil.getDetailsForSubElement(child);

                        for (int j = 0; j < details.length; j++)
                        {
                            if (!indirectAttributes.contains(details[j][0]))
                                indirectAttributes.add(details[j][0]);
                        }
                    }
                }

            }
        }

        return indirectAttributes.toArray(new String[indirectAttributes.size()]);
    }

    private List<CustomCheckBox> sortCheckBoxesAlphabetically(List<CustomCheckBox> list)
    {
        List<CustomCheckBox> result = new ArrayList<CustomCheckBox>();
        String[] names = new String[list.size()];

        for (int c = 0; c < list.size(); c++)
        {
            names[c] = list.get(c).getText();
        }

        Arrays.sort(names);

        for (int c = 0; c < names.length; c++)
        {
            CustomCheckBox cb = new CustomCheckBox(names[c]);
            result.add(c, cb);
        }

        return result;
    }

    /**
     * Creates a new instance of FilterChildren (modal dialog)
     * 
     * @return A Map of selected elements -> selected attributes (check boxes checked or unchecked by the user)
     */
    public Map<String, String[]> showUI()
    {
        _ui.setModalityType(ModalityType.APPLICATION_MODAL);
        _ui.setModal(true);
        _ui.setVisible(true);
        return _result;
    }
}
