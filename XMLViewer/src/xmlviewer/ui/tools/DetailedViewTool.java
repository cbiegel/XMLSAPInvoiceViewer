package xmlviewer.ui.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xmlviewer.tree.util.DetailedViewUtil;
import xmlviewer.ui.DetailedView;


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

    public DetailedViewTool(JTree tree)
    {
        _ui = new DetailedView();
        _tree = tree;

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

        addComboBoxListener(elementComboBox);
        addSubElementListListener(subElementList);
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
                    String[] listData = DetailedViewUtil.getSubElementsListFromTree(elementNode);
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
            };

        _ui.getElementDetailTable().setModel(tableModel);

    }
}
