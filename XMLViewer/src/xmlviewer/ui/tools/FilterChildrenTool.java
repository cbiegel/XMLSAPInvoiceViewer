package xmlviewer.ui.tools;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xmlviewer.tree.util.DetailedViewUtil;
import xmlviewer.ui.detail.FilterChildren;


public class FilterChildrenTool
{
    private FilterChildren _ui;
    private Node _parentNode;
    private int _parentNodeIndex;
    private Map<JCheckBox, Set<JCheckBox>> _childrenAttributesMap;
    private String[] _result;

    public FilterChildrenTool(Node parentNode, int parentNodeIndex, JFrame owner)
    {
        _parentNode = parentNode;
        _parentNodeIndex = parentNodeIndex;
        _childrenAttributesMap = new HashMap<JCheckBox, Set<JCheckBox>>();
        _ui = new FilterChildren(owner);
        initializeUI();
        setupListeners();
    }

    private void initializeUI()
    {
        _ui.getUIParentNameLabel().setText(_parentNode.getNodeName());

        String[] children = DetailedViewUtil.getSubElementsListFromTree(_parentNode, false);
        String[] trimmedData = new String[children.length];

        for (int c = 0; c < children.length; c++)
        {
            String trimmedString = DetailedViewUtil.stripStringToLetters(children[c]);
            trimmedData[c] = trimmedString;
        }

        List<String> childrenList = getChildrenList(trimmedData);

        for (String s : childrenList)
        {
            JCheckBox checkBox = new JCheckBox(s);
            String[] childNodes = DetailedViewUtil.getSubElementsListFromTree(_parentNode, false);
            String[] directAttributes = getDirectAttributesSetForElement(childNodes, checkBox.getText());
            String[] indirectAttributes = getIndirectAttributesSetForElement(childNodes, checkBox.getText());
            Set<JCheckBox> checkBoxAttributes = new HashSet<JCheckBox>();

            // put entry in map
            for (int c = 0; c < directAttributes.length; c++)
            {
                JCheckBox cb = new JCheckBox(directAttributes[c]);
                checkBoxAttributes.add(cb);
            }
            for (int c = 0; c < indirectAttributes.length; c++)
            {
                JCheckBox cb = new JCheckBox(indirectAttributes[c]);
                checkBoxAttributes.add(cb);
            }
            _childrenAttributesMap.put(checkBox, checkBoxAttributes);

            _ui.getUIChildrenCheckBoxList().addCheckBox(checkBox);
        }
    }

    private void setupListeners()
    {
        JButton okButton = _ui.getUIOkButton();

        addOKButtonListener(okButton);

        for (JCheckBox checkBox : _ui.getUIChildrenCheckBoxList().getAllCheckBoxes())
        {
            addChildrenCheckBoxListener(checkBox);
        }

    }

    private void addOKButtonListener(JButton button)
    {
        button.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                JCheckBox[] selectedAttributes = _ui.getUIAttributesCheckBoxList().getSelectedCheckBoxes();
                String[] result = new String[selectedAttributes.length];

                for (int c = 0; c < selectedAttributes.length; c++)
                {
                    result[c] = selectedAttributes[c].getText();
                }

                _result = result;
                _ui.dispose();
            }
        });
    }

    private void addChildrenCheckBoxListener(final JCheckBox checkBox)
    {
        checkBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event)
            {
                // List<JCheckBox> selectedChildren = new ArrayList<JCheckBox>();
                // for (JCheckBox cb : _childrenAttributesMap.keySet())
                // {
                // if (cb.isSelected())
                // {
                // selectedChildren.add(cb);
                // }
                // }
                //
                // List<Set<JCheckBox>> attributesList = new ArrayList<Set<JCheckBox>>();
                // for (JCheckBox cb : selectedChildren)
                // {
                // Set<JCheckBox> attributes = new HashSet<JCheckBox>();
                //
                // for (JCheckBox checkBox : _childrenAttributesMap.get(cb))
                // {
                // attributes.add(checkBox);
                // }
                //
                // attributesList.add(attributes);
                // }
                // _ui.getUIAttributesCheckBoxList().makeUnionAndDisplayData(attributesList);

                List<JCheckBox> selectedChildren = new ArrayList<JCheckBox>();
                Map<JCheckBox, List<JCheckBox>> selectedChildrenMap = new HashMap<JCheckBox, List<JCheckBox>>();

                for (JCheckBox cb : _childrenAttributesMap.keySet())
                {
                    if (cb.isSelected())
                    {
                        selectedChildren.add(cb);
                    }
                }

                for (JCheckBox cb : selectedChildren)
                {
                    List<JCheckBox> attributes = new ArrayList<JCheckBox>();
                    attributes.addAll(_childrenAttributesMap.get(cb));
                    selectedChildrenMap.put(cb, attributes);
                }

                _ui.getUIAttributesCheckBoxList().displayCheckBoxComponents(selectedChildrenMap);
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
            String formattedChildName = DetailedViewUtil.formatChildName(DetailedViewUtil.stripStringToLetters(childNodes[c]));
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
            String formattedChildName = DetailedViewUtil.formatChildName(DetailedViewUtil.stripStringToLetters(childNodes[c]));
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

    public String[] showUI()
    {
        _ui.setModalityType(ModalityType.APPLICATION_MODAL);
        _ui.setModal(true);
        _ui.setVisible(true);
        return _result;
    }
}
