package xmlviewer.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;


public class DetailedView extends JPanel {
    private final JTable _detailTable;
    private final JLabel _elementNumberLabel;
    private final JComboBox<String> _elementComboBox;
    private final JList _elementChildrenList;
    private JLabel _elementLabel;

    /**
     * Create the panel.
     */
    public DetailedView() {

        JPanel elementLabelPanel = new JPanel();
        elementLabelPanel.setBackground(SystemColor.menu);

        _elementLabel = new JLabel("Element");
        _elementLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

        _elementNumberLabel = new JLabel("1");
        _elementNumberLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        GroupLayout gl_elementLabelPanel = new GroupLayout(elementLabelPanel);
        gl_elementLabelPanel.setHorizontalGroup(
                gl_elementLabelPanel.createParallelGroup(Alignment.LEADING)
                        .addGap(0, 180, Short.MAX_VALUE)
                        .addGroup(gl_elementLabelPanel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(_elementLabel, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(_elementNumberLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(67, Short.MAX_VALUE))
                );
        gl_elementLabelPanel.setVerticalGroup(
                gl_elementLabelPanel.createParallelGroup(Alignment.LEADING)
                        .addGap(0, 51, Short.MAX_VALUE)
                        .addGroup(gl_elementLabelPanel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_elementLabelPanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(_elementLabel, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                                        .addComponent(_elementNumberLabel))
                                .addContainerGap())
                );
        elementLabelPanel.setLayout(gl_elementLabelPanel);

        JPanel elementSelectionPanel = new JPanel();
        elementSelectionPanel.setBackground(SystemColor.menu);

        _elementComboBox = new JComboBox();
        _elementComboBox.setMaximumRowCount(6);
        GroupLayout gl_elementSelectionPanel = new GroupLayout(elementSelectionPanel);
        gl_elementSelectionPanel.setHorizontalGroup(
                gl_elementSelectionPanel.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_elementSelectionPanel.createSequentialGroup()
                                .addContainerGap(270, Short.MAX_VALUE)
                                .addComponent(_elementComboBox, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                );
        gl_elementSelectionPanel.setVerticalGroup(
                gl_elementSelectionPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_elementSelectionPanel.createSequentialGroup()
                                .addGap(5)
                                .addComponent(_elementComboBox, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
        elementSelectionPanel.setLayout(gl_elementSelectionPanel);

        JSplitPane listTableSplitPane = new JSplitPane();
        listTableSplitPane.setResizeWeight(0.03);

        JLabel lblSelectAnSapinvoiceitem = new JLabel("SapInvoiceExternals elements:");
        lblSelectAnSapinvoiceitem.setFont(new Font("Tahoma", Font.PLAIN, 14));
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(
                            groupLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(
                                        groupLayout.createParallelGroup(Alignment.LEADING)
                                                .addGroup(
                                                    groupLayout.createSequentialGroup()
                                                            .addComponent(
                                                                listTableSplitPane,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                837,
                                                                Short.MAX_VALUE)
                                                            .addContainerGap())
                                                .addGroup(
                                                    groupLayout.createSequentialGroup()
                                                            .addComponent(
                                                                elementLabelPanel,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(ComponentPlacement.RELATED, 276, Short.MAX_VALUE)
                                                            .addComponent(lblSelectAnSapinvoiceitem)
                                                            .addPreferredGap(ComponentPlacement.RELATED)
                                                            .addComponent(
                                                                elementSelectionPanel,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                148,
                                                                GroupLayout.PREFERRED_SIZE)
                                                            .addGap(16))))
                );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(
                            groupLayout.createSequentialGroup()
                                    .addGroup(
                                        groupLayout.createParallelGroup(Alignment.LEADING)
                                                .addGroup(
                                                    groupLayout.createSequentialGroup()
                                                            .addContainerGap()
                                                            .addGroup(
                                                                groupLayout.createParallelGroup(Alignment.LEADING)
                                                                        .addComponent(
                                                                            elementSelectionPanel,
                                                                            GroupLayout.PREFERRED_SIZE,
                                                                            52,
                                                                            GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(
                                                                            elementLabelPanel,
                                                                            GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.DEFAULT_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(groupLayout.createSequentialGroup()
                                                        .addGap(28)
                                                        .addComponent(lblSelectAnSapinvoiceitem)))
                                    .addGap(18)
                                    .addComponent(listTableSplitPane, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                                    .addContainerGap())
                );

        JScrollPane listScrollPane = new JScrollPane();
        listTableSplitPane.setLeftComponent(listScrollPane);
        listScrollPane.setViewportBorder(new LineBorder(Color.LIGHT_GRAY));

        _elementChildrenList = new JList();
        listScrollPane.setViewportView(_elementChildrenList);

        JScrollPane tableScrollPane = new JScrollPane();
        listTableSplitPane.setRightComponent(tableScrollPane);
        tableScrollPane.setViewportBorder(new LineBorder(new Color(192, 192, 192)));

        _detailTable = new JTable();
        _detailTable.setShowGrid(false);
        _detailTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        _detailTable.setFillsViewportHeight(true);
        _detailTable.setRowHeight(20);
        _detailTable.setAutoCreateRowSorter(true);
        tableScrollPane.setViewportView(_detailTable);
        setLayout(groupLayout);

    }

    public JTable getElementDetailTable() {
        return _detailTable;
    }

    public JLabel getElementNumberLabel() {
        return _elementNumberLabel;
    }

    public JComboBox<String> getElementComboBox() {
        return _elementComboBox;
    }

    public JList getElementChildrenList() {
        return _elementChildrenList;
    }

    public JLabel getElementLabel()
    {
        return _elementLabel;
    }
}
