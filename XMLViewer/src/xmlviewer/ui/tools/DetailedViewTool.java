package xmlviewer.ui.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xmlviewer.tree.util.DetailedViewUtil;
import xmlviewer.ui.detail.DetailedView;


/**
 * @author cbiegel
 *         The tool for the DetailedView UI. Contains logic such as listeners and defines behaviours for the UI
 *         components.
 * 
 */
public class DetailedViewTool
{
    private DetailedView _ui;
    private JTree _tree;
    private Map<Integer, Node> _nodeMap;
    private int _selectedTableRow;
    private JFrame _parentFrame;

    public DetailedViewTool(JTree tree, JFrame parent)
    {
        _ui = new DetailedView();
        _tree = tree;
        _nodeMap = new HashMap<Integer, Node>();
        _selectedTableRow = -1;
        _parentFrame = parent;

        setupListeners();
        fillComboBoxData();
    }

    public DetailedView getUI()
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

        addComboBoxListener(elementComboBox);
        addSubElementListListener(subElementList);
        addElementDetailTableListener(elementDetailTable);
    }

    private void addComboBoxListener(final JComboBox comboBox)
    {
        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event)
            {
                // update the element label
                if (comboBox.getSelectedIndex() == 0)
                {
                    _ui.getElementLabel().setText("Meta Info");
                    _ui.getElementNumberLabel().setText("");

                    // fill the elementTable with data
                    fillTableWithMetaInfoData();

                    // reset list data
                    _ui.getElementChildrenList().setListData(new String[0]);
                }
                else
                {
                    _ui.getElementLabel().setText("Element");
                    _ui.getElementNumberLabel().setText(String.valueOf(comboBox.getSelectedIndex()));

                    // fill the elementList with data
                    Node elementNode = getElementNode((comboBox.getSelectedIndex() - 1));
                    String[] listData = DetailedViewUtil.getSubElementsListFromTree(elementNode, true);
                    _nodeMap = DetailedViewUtil.getNodeMap();

                    _ui.getElementChildrenList().setListData(listData);

                    // reset table data
                    fillTableWithData(new String[0][0]);
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
                if (list.getSelectedValue() != null)
                {
                    int nodeCount = list.getSelectedIndex();
                    Node selectedElement = _nodeMap.get(nodeCount);
                    String[][] tableData = DetailedViewUtil.getDetailsForSubElement(selectedElement);

                    fillTableWithData(tableData);
                }
                else
                {
                    if (_ui.getElementComboBox().getSelectedIndex() > 0)
                    {
                        fillTableWithData(new String[0][0]);
                    }
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
                String strippedName = DetailedViewUtil.stripStringToLetters(node.getNodeName());
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
                    new FilterChildrenTool(parentNode, _ui.getElementChildrenList().getSelectedIndex(), _parentFrame);
                String[] result = filterTool.showUI();

                // TODO: Ergebnis des FilterTools verarbeiten

                if (result != null)
                {
                    for (String string : result) {
                        System.out.println(string);
                    }

                }
            }
        });

        popup.add(filterButton);
        return popup;
    }
}
