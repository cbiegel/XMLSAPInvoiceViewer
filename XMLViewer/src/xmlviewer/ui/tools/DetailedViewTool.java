package xmlviewer.ui.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xmlviewer.tree.util.DetailedViewUtil;
import xmlviewer.ui.detail.DetailedViewPanel;
import xmlviewer.ui.main.MainWindow;
import xmlviewer.ui.main.XMLViewerMenu;


/**
 * @author cbiegel
 *         The tool for the DetailedView UI. Contains logic such as listeners and defines behaviours for the UI
 *         components.
 * 
 */
public class DetailedViewTool implements Observer
{
    private DetailedViewPanel _ui;
    private JTree _tree;
    private Map<Integer, Node> _nodeMap;
    private int _selectedTableRow;
    private MainWindow _parentWindow;
    private Map<String, String[]> _filterMap;
    private String[] _filteredList;
    private FindElementTool _findElementTool;

    public DetailedViewTool(JTree tree, MainWindow parent)
    {
        _ui = new DetailedViewPanel();
        _tree = tree;
        _nodeMap = new HashMap<Integer, Node>();
        _selectedTableRow = -1;
        _parentWindow = parent;
        _filteredList = new String[0];

        setupListeners();
        fillComboBoxData();
    }

    public DetailedViewPanel getUI()
    {
        return _ui;
    }

    /**
     * Fills the combo box of the UI with data (names of the elements of the UI's tree).
     */
    private void fillComboBoxData()
    {
        String[] data = DetailedViewUtil.getElementItemsFromTree(_tree);
        DefaultComboBoxModel model = new DefaultComboBoxModel(data);
        _ui.getElementComboBox().setModel(model);
        _ui.getElementComboBox().setSelectedIndex(0);
    }

    /**
     * Sets up various listeners for the UI's components.
     */
    private void setupListeners()
    {
        JComboBox elementComboBox = _ui.getElementComboBox();
        JList subElementList = _ui.getElementChildrenList();
        JTable elementDetailTable = _ui.getElementDetailTable();
        JMenuItem findElement = _parentWindow.getViewerMenu().getViewFindItem();
        JCheckBoxMenuItem applyFilter = _parentWindow.getViewerMenu().getViewApplyFilterItem();
        JCheckBoxMenuItem showCompact = _parentWindow.getViewerMenu().getViewShowCompactItem();

        addComboBoxListener(elementComboBox);
        addSubElementListListener(subElementList);
        addElementDetailTableListener(elementDetailTable);
        addFindElementListener(findElement);
        addApplyFilterListener(applyFilter);
        addShowCompactListener(showCompact);
    }

    private void addComboBoxListener(final JComboBox comboBox)
    {
        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                // update the element label (META INFO)
                if (comboBox.getSelectedIndex() == 0)
                {
                    _ui.getElementLabel().setText("Meta Info");
                    _ui.getElementNumberLabel().setText("");
                    _parentWindow.getViewerMenu().getViewApplyFilterItem().setSelected(false);
                    _parentWindow.getViewerMenu().getViewApplyFilterItem().setEnabled(false);
                    _parentWindow.getViewerMenu().getViewFindItem().setEnabled(false);

                    // fill the elementTable with data
                    fillTableWithMetaInfoData();

                    // reset list data
                    _ui.getElementChildrenList().setListData(new String[0]);

                    updateElementDetailLabel();
                }
                // update the element label (ELEMENT INFO)
                else
                {
                    _ui.getElementLabel().setText("Element");
                    _ui.getElementNumberLabel().setText(String.valueOf(comboBox.getSelectedIndex()));
                    _parentWindow.getViewerMenu().getViewApplyFilterItem().setSelected(false);
                    _parentWindow.getViewerMenu().getViewApplyFilterItem().setEnabled(false);
                    _parentWindow.getViewerMenu().getViewFindItem().setEnabled(true);

                    // fill the elementList with data
                    Node elementNode = getElementNode((comboBox.getSelectedIndex() - 1));
                    String[] listData = DetailedViewUtil.getSubElementsListFromTree(elementNode, true, false);
                    _nodeMap = DetailedViewUtil.getNodeMap();

                    _ui.getElementChildrenList().setListData(listData);

                    // reset table data
                    fillTableWithData(new String[0][0]);

                    updateElementDetailLabel();
                }
            }

        });
    }

    private Node getElementNode(int elementNumber)
    {
        Node root = (Node) _tree.getModel().getRoot();
        NodeList children = root.getChildNodes();
        Node sapInvoiceExternals = children.item(3);
        NodeList sapInvoiceElements = sapInvoiceExternals.getChildNodes();

        return sapInvoiceElements.item(elementNumber);
    }

    private void addSubElementListListener(final JList list)
    {
        list.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent event)
            {
                displayTable(list);
                JCheckBoxMenuItem showCompact = _parentWindow.getViewerMenu().getViewShowCompactItem();

                if (!list.isSelectionEmpty())
                {
                    showCompact.setEnabled(true);
                }
                else
                {
                    showCompact.setSelected(false);
                    showCompact.setEnabled(false);
                }
            }

        });

        list.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.isMetaDown())
                {
                    list.setSelectedIndex(list.locationToIndex(e.getPoint()));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    list.setSelectedIndex(list.locationToIndex(e.getPoint()));
                    int selectedIndex = list.getSelectedIndex();

                    if (selectedIndex < 0)
                    {
                        return;
                    }

                    Node selectedNode = _nodeMap.get(selectedIndex);

                    if (isInvoiceItemOrPriceExternal(selectedNode))
                    {
                        JPopupMenu filterPopup = createFilterChildrenPopup(selectedNode);

                        filterPopup.show(list, e.getX(), e.getY());
                    }
                }
            }

            private boolean isInvoiceItemOrPriceExternal(Node node)
            {
                String strippedName = DetailedViewUtil.stripStringToLettersAndNumbers(node.getNodeName());
                return (strippedName.equals("mSapInvoiceItems") || strippedName.equals("mSapInvoiceItemRevenueDatas"));
            }
        });

        list.addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (e.isMetaDown())
                {
                    list.setSelectedIndex(list.locationToIndex(e.getPoint()));
                }
            }
        });
    }

    private void addElementDetailTableListener(final JTable table)
    {
        table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (_selectedTableRow == table.getSelectedRow())
                {
                    table.clearSelection();
                    _selectedTableRow = -1;
                }
                else
                {
                    _selectedTableRow = table.getSelectedRow();
                    if (_selectedTableRow >= 0)
                    {
                        table.setRowSelectionInterval(_selectedTableRow, _selectedTableRow);
                    }
                    else
                    {
                        int maxRow = table.getRowCount() - 1;
                        table.setRowSelectionInterval(maxRow, maxRow);
                    }
                }
            }
        });
    }

    private void addFindElementListener(final JMenuItem findItem)
    {
        findItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                _findElementTool = new FindElementTool(_parentWindow.getFrame(), DetailedViewTool.this);
            }
        });
    }

    private void addApplyFilterListener(final JCheckBoxMenuItem checkBox)
    {
        checkBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event)
            {
                // display the whole unfiltered list
                if (!checkBox.isSelected())
                {
                    Node elementNode = getElementNode((_ui.getElementComboBox().getSelectedIndex() - 1));
                    String[] listData = DetailedViewUtil.getSubElementsListFromTree(elementNode, true, false);
                    _nodeMap = DetailedViewUtil.getNodeMap();

                    _ui.getElementChildrenList().setListData(listData);

                    // reset table data
                    fillTableWithData(new String[0][0]);

                    updateElementDetailLabel();
                }
                // display the filtered list
                else
                {
                    updateElementDetailLabel();
                    _ui.getElementChildrenList().setListData(_filteredList);
                }
            }
        });
    }

    private void addShowCompactListener(final JCheckBoxMenuItem checkBox)
    {
        checkBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event)
            {
                Node selectedNode = _nodeMap.get(_ui.getElementChildrenList().getSelectedIndex());
                String[][] originalTableData = DetailedViewUtil.getDetailsForSubElement(selectedNode);
                JMenuItem filterItem = _parentWindow.getViewerMenu().getViewApplyFilterItem();

                // compact view is active
                if (checkBox.isSelected())
                {
                    String[][] compactTableData = null;

                    // filter is active
                    if (filterItem.isSelected())
                    {
                        String[][] filteredData = DetailedViewUtil.getTableData(_ui.getElementDetailTable());
                        compactTableData = DetailedViewUtil.getCompactAttributes(filteredData);
                    }
                    // filter is not active
                    else
                    {
                        compactTableData = DetailedViewUtil.getCompactAttributes(originalTableData);
                    }

                    fillTableWithData(compactTableData);
                    updateElementDetailLabel();
                }
                // compact view is not active
                else
                {
                    // filter is active
                    if (filterItem.isSelected())
                    {
                        displayTable(_ui.getElementChildrenList());
                    }
                    // filter is not active
                    else
                    {
                        fillTableWithData(originalTableData);
                    }

                    updateElementDetailLabel();
                }
            }
        });
    }

    private void fillTableWithData(String[][] data)
    {
        @SuppressWarnings("serial")
        DefaultTableModel tableModel =
            new DefaultTableModel(data, new String[] {"Attributes", "Values"})
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    return false;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex)
                {
                    return String.class;
                }
            };

        _ui.getElementDetailTable().setModel(tableModel);
    }

    private void fillTableWithMetaInfoData()
    {
        Node root = (Node) _tree.getModel().getRoot();
        NodeList children = root.getChildNodes();

        String[][] tableData = new String[(children.getLength() - 1)][2];

        for (int c = 0; c < (children.getLength() - 1); c++)
        {
            Node child = children.item(c);
            tableData[c][0] = child.getNodeName();
            tableData[c][1] = child.getFirstChild().getNodeValue();
        }

        @SuppressWarnings("serial")
        DefaultTableModel tableModel =
            new DefaultTableModel(tableData, new String[] {"Attributes", "Values"})
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    return false;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex)
                {
                    return String.class;
                }
            };

        _ui.getElementDetailTable().setModel(tableModel);

    }

    private void displayTable(JList list)
    {
        // filter is not active
        if (!_parentWindow.getViewerMenu().getViewApplyFilterItem().isSelected())
        {
            if (list.getSelectedValue() != null)
            {
                int nodePosition = list.getSelectedIndex();
                Node selectedElement = _nodeMap.get(nodePosition);
                String[][] tableData = DetailedViewUtil.getDetailsForSubElement(selectedElement);

                // compact view is active
                if (_parentWindow.getViewerMenu().getViewShowCompactItem().isSelected())
                {
                    String[][] compactData = DetailedViewUtil.getCompactAttributes(tableData);
                    fillTableWithData(compactData);
                }
                // compact view is not active
                else
                {
                    fillTableWithData(tableData);
                }

            }
            else
            {
                if (_ui.getElementComboBox().getSelectedIndex() > 0)
                {
                    fillTableWithData(new String[0][0]);
                }
            }
        }
        // filter is active
        else
        {
            if (list.getSelectedValue() != null)
            {
                int nodePosition = list.getSelectedIndex();
                Node selectedElement = _nodeMap.get(nodePosition);
                String[][] tableData = DetailedViewUtil.getDetailsForSubElement(selectedElement);

                // parent attributes are not to be filtered
                if (nodePosition == 0)
                {
                    fillTableWithData(tableData);
                }
                // direct children of the parent element are not to be filtered
                else if (DetailedViewUtil.isDirectChild(selectedElement, _nodeMap.get(0)))
                {
                    fillTableWithData(tableData);
                }
                else
                {
                    String[] filter = _filterMap.get(selectedElement.getNodeName());
                    // treat elements ("el") the same way as their parents
                    if (filter == null)
                    {
                        filter = _filterMap.get(selectedElement.getParentNode().getNodeName());
                    }
                    // apply the filter
                    if (filter != null)
                    {
                        String[][] filteredData =
                            makeIntersection(tableData, filter);

                        // compact view is active
                        if (_parentWindow.getViewerMenu().getViewShowCompactItem().isSelected())
                        {
                            String[][] compactData = DetailedViewUtil.getCompactAttributes(filteredData);
                            fillTableWithData(compactData);
                        }
                        // compact view is not active
                        else
                        {
                            fillTableWithData(filteredData);
                        }
                    }
                    // selected element has not been selected for filtering
                    // (the user did not check the checkBox for this element in the filter dialog)
                    else
                    {
                        // compact view is active
                        if (_parentWindow.getViewerMenu().getViewShowCompactItem().isSelected())
                        {
                            String[][] compactData = DetailedViewUtil.getCompactAttributes(tableData);
                            fillTableWithData(compactData);
                        }
                        // compact view is not active
                        else
                        {
                            fillTableWithData(tableData);
                        }
                    }
                }
            }
        }
    }

    private JPopupMenu createFilterChildrenPopup(final Node parentNode)
    {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem filterButton = new JMenuItem("Filter children...");
        filterButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                FilterChildrenTool filterTool =
                    new FilterChildrenTool(parentNode, _ui.getElementChildrenList().getSelectedIndex(),
                        _parentWindow.getFrame());
                _filterMap = filterTool.showUI();

                if (_filterMap != null)
                {
                    if (!_filterMap.isEmpty())
                    {
                        String[] children = DetailedViewUtil.getSubElementsListFromTree(parentNode, true, true);
                        List<String> display = new ArrayList<String>();
                        display.add(parentNode.getNodeName());
                        for (int c = 0; c < children.length; c++)
                        {
                            display.add(children[c]);
                        }

                        _filteredList = display.toArray(new String[display.size()]);

                        _ui.getElementChildrenList().setListData(_filteredList);
                        fillTableWithData(new String[0][0]);

                        updateElementDetailLabel();

                        _parentWindow.getViewerMenu().getViewApplyFilterItem().setEnabled(true);
                        _parentWindow.getViewerMenu().getViewApplyFilterItem().setSelected(true);
                    }
                }
            }
        });

        popup.add(filterButton);
        return popup;
    }

    private String[][] makeIntersection(String[][] fullData, String[] filter)
    {
        List<String> resultList = new ArrayList<String>();
        for (int i = 0; i < fullData.length; i++)
        {
            for (int c = 0; c < filter.length; c++)
            {
                if (fullData[i][0].equals(filter[c]))
                {
                    resultList.add(fullData[i][0]);
                    resultList.add(fullData[i][1]);
                }
            }

        }

        String[][] result = DetailedViewUtil.convertListTo2DStringArray(resultList);

        return result;
    }

    private void updateElementDetailLabel()
    {
        String elementDetailLabel = "";
        XMLViewerMenu menu = _parentWindow.getViewerMenu();

        if (menu.getViewApplyFilterItem().isSelected())
        {
            elementDetailLabel += "(filtered)";

            if (menu.getViewShowCompactItem().isSelected())
            {
                elementDetailLabel += " (compact)";
            }
        }
        else if (menu.getViewShowCompactItem().isSelected())
        {
            elementDetailLabel += "(compact)";
        }

        _ui.getElementDetailLabel().setText(elementDetailLabel);
    }

    @Override
    public void update(Observable observable, Object searchTextObject)
    {
        if (searchTextObject instanceof String)
        {
            String searchText = (String) searchTextObject;
            JList elementList = _ui.getElementChildrenList();
            ListModel listModel = elementList.getModel();
            boolean wrapped = false;

            // search the list for the next occurrence of searchText
            // if no element in the list is currently selected, the search will start from the top of the list
            if (elementList.isSelectionEmpty())
            {
                for (int c = 0; c < listModel.getSize(); c++)
                {

                    // search the list
                    if (searchElementList(c, searchText))
                    {
                        _findElementTool.setStatusLabel("");
                        break;
                    }
                    // search the table
                    else if (_findElementTool.isSearchAttributesSelected())
                    {
                        Node element = _nodeMap.get(c);
                        String[][] attributes = DetailedViewUtil.getDetailsForSubElement(element);
                        boolean attributeMatchFound = false;

                        for (int i = 0; i < attributes.length; i++)
                        {
                            if (searchAttributes(element, c, i, searchText))
                            {
                                attributeMatchFound = true;
                                _findElementTool.setStatusLabel("");
                                break;
                            }
                        }

                        if (attributeMatchFound)
                        {
                            break;
                        }
                    }

                    // last element reached, no match found
                    if (c == (listModel.getSize() - 1))
                    {
                        _findElementTool.setStatusLabel("No match found");
                    }
                }
            }
            // if an element is already selected, we will start the search from there
            else
            {
                int startIndex = elementList.getSelectedIndex();

                // determine the start index for the upcoming loop.
                // if the currently selected element is the last entry of the list, start at the beginning
                if (startIndex == (listModel.getSize() - 1))
                {
                    if (_findElementTool.isWrapSearchSelected())
                    {
                        startIndex = 0;
                    }
                    else
                    {
                        startIndex = elementList.getSelectedIndex();
                    }
                }
                else
                {
                    ++startIndex;
                }

                // start the iteration through the list
                for (int c = startIndex; c < listModel.getSize(); c++)
                {
                    if (c == 0)
                    {
                        wrapped = true;
                        _findElementTool.setStatusLabel("Wrapped search");
                    }

                    int currentListSelectionIndex = elementList.getSelectedIndex();

                    // search the table (attributes)
                    if (_findElementTool.isSearchAttributesSelected())
                    {
                        int currentListIndex = (c - 1) >= 0 ? (c - 1) : 0;
                        Node element = _nodeMap.get(currentListIndex);
                        String[][] attributes = DetailedViewUtil.getDetailsForSubElement(element);
                        boolean attributeMatchFound = false;
                        int attributeStartIndex = _ui.getElementDetailTable().getSelectedRow() + 1;
                        if (currentListSelectionIndex != currentListIndex)
                        {
                            attributeStartIndex = 0;
                        }

                        // start the iteration through the table
                        for (int i = attributeStartIndex; i < attributes.length; i++)
                        {
                            if (searchAttributes(element, currentListIndex, i, searchText))
                            {
                                if (!wrapped)
                                {
                                    _findElementTool.setStatusLabel("");
                                }
                                else
                                {
                                    wrapped = false;
                                }

                                attributeMatchFound = true;
                                break;
                            }
                        }

                        if (attributeMatchFound)
                        {
                            break;
                        }
                    }

                    // search the list
                    if (searchElementList(c, searchText))
                    {
                        int newSelectionIndex = elementList.getSelectedIndex();
                        if (currentListSelectionIndex == newSelectionIndex)
                        {
                            _findElementTool.setStatusLabel("Last match reached");
                        }
                        else
                        {
                            if (!wrapped)
                            {
                                _findElementTool.setStatusLabel("");
                            }
                            else
                            {
                                wrapped = false;
                            }

                            break;
                        }
                    }

                    // wrap search, last element reached
                    if (c == (listModel.getSize() - 1))
                    {
                        if (_findElementTool.isWrapSearchSelected())
                        {

                            // this is the second time the loop reaches this point without finding a match
                            if (wrapped)
                            {
                                _findElementTool.setStatusLabel("No match found");
                                wrapped = false;
                                break;
                            }

                            c = -1;
                        }
                        else
                        {
                            _findElementTool.setStatusLabel("Last match reached");
                        }
                    }
                }
            }
        }
        // this should never happen
        else
        {
            System.err.println("ERROR: searchText object is not a String. How could this happen? :(");
            return;
        }
    }

    /**
     * Compares the entry at the given index with searchText. If they match, returns true. If not, returns false.
     * This method takes into account the various options from the search window (the JCheckBoxes).
     */
    private boolean searchElementList(int currentEntryIndex, String searchText)
    {
        JList elementList = _ui.getElementChildrenList();
        String currentEntry = (String) elementList.getModel().getElementAt(currentEntryIndex);
        currentEntry = DetailedViewUtil.stripStringToLettersAndNumbers(currentEntry);

        return findMatch(currentEntry, searchText, 0, currentEntryIndex, 0);
    }

    private boolean searchAttributes(Node parentElement, int currentListIndex, int currentAttributeIndex, String searchText)
    {
        String[][] attributes = DetailedViewUtil.getDetailsForSubElement(parentElement);
        String currentEntry = attributes[currentAttributeIndex][0];
        currentEntry = DetailedViewUtil.stripStringToLettersAndNumbers(currentEntry);

        return findMatch(currentEntry, searchText, 1, currentListIndex, currentAttributeIndex);
    }

    /**
     * @param searchMode
     *            0 = list , 1 = attributes
     */
    private boolean findMatch(String s1, String s2, int searchMode, int currentListIndex, int currentAttributeIndex)
    {
        JList elementList = _ui.getElementChildrenList();
        JTable attributeTable = null;

        // case sensitive enabled
        if (_findElementTool.isCaseSensitiveSelected())
        {
            // whole match enabled
            if (_findElementTool.isWholeMatchSelected())
            {
                // match found
                if (s1.equals(s2))
                {
                    if (searchMode == 0)
                    {
                        elementList.setSelectedIndex(currentListIndex);
                    }
                    else if (searchMode == 1)
                    {
                        elementList.setSelectedIndex(currentListIndex);
                        attributeTable = _ui.getElementDetailTable();
                        attributeTable.setRowSelectionInterval(currentAttributeIndex, currentAttributeIndex);
                    }

                    return true;
                }
            }
            // whole match disabled
            else
            {
                if (s1.contains(s2))
                {
                    if (searchMode == 0)
                    {
                        elementList.setSelectedIndex(currentListIndex);
                    }
                    else if (searchMode == 1)
                    {
                        elementList.setSelectedIndex(currentListIndex);
                        attributeTable = _ui.getElementDetailTable();
                        attributeTable.setRowSelectionInterval(currentAttributeIndex, currentAttributeIndex);
                    }

                    return true;
                }
            }
        }
        // case sensitive disabled
        else
        {
            // whole match enabled
            if (_findElementTool.isWholeMatchSelected())
            {
                // match found
                if (s1.toLowerCase().equals(s2.toLowerCase()))
                {
                    if (searchMode == 0)
                    {
                        elementList.setSelectedIndex(currentListIndex);
                    }
                    else if (searchMode == 1)
                    {
                        elementList.setSelectedIndex(currentListIndex);
                        attributeTable = _ui.getElementDetailTable();
                        attributeTable.setRowSelectionInterval(currentAttributeIndex, currentAttributeIndex);
                    }

                    return true;
                }
            }
            // whole match disabled
            else
            {
                if (s1.toLowerCase().contains(s2))
                {
                    if (searchMode == 0)
                    {
                        elementList.setSelectedIndex(currentListIndex);
                    }
                    else if (searchMode == 1)
                    {
                        elementList.setSelectedIndex(currentListIndex);
                        attributeTable = _ui.getElementDetailTable();
                        attributeTable.setRowSelectionInterval(currentAttributeIndex, currentAttributeIndex);
                    }

                    return true;
                }
            }
        }

        // no match
        return false;
    }
}
