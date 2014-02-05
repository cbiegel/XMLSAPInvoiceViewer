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
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;


public class DetailedView extends JPanel {
    private final JTable _detailTable;
    private final JLabel _elementNumberLabel;
    private final JComboBox<String> _elementComboBox;
    private final JList _elementChildrenList;
    private JLabel _tableHeaderLabel;
    private JLabel _elementLabel;

    /**
     * Create the panel.
     */
    public DetailedView() {

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.menu);

        _elementLabel = new JLabel("Element");
        _elementLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

        _elementNumberLabel = new JLabel("1");
        _elementNumberLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGap(0, 180, Short.MAX_VALUE)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(_elementLabel, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(_elementNumberLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(67, Short.MAX_VALUE))
                );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGap(0, 51, Short.MAX_VALUE)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(_elementLabel, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                                        .addComponent(_elementNumberLabel))
                                .addContainerGap())
                );
        panel.setLayout(gl_panel);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(SystemColor.menu);

        _elementComboBox = new JComboBox();
        _elementComboBox.setMaximumRowCount(5);
        GroupLayout gl_panel_1 = new GroupLayout(panel_1);
        gl_panel_1.setHorizontalGroup(
                gl_panel_1.createParallelGroup(Alignment.TRAILING)
                        .addGap(0, 168, Short.MAX_VALUE)
                        .addGroup(gl_panel_1.createSequentialGroup()
                                .addContainerGap(29, Short.MAX_VALUE)
                                .addComponent(_elementComboBox, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                );
        gl_panel_1.setVerticalGroup(
                gl_panel_1.createParallelGroup(Alignment.LEADING)
                        .addGap(0, 52, Short.MAX_VALUE)
                        .addGroup(gl_panel_1.createSequentialGroup()
                                .addGap(5)
                                .addComponent(_elementComboBox, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
        panel_1.setLayout(gl_panel_1);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.03);
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
                                                                splitPane,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                1075,
                                                                Short.MAX_VALUE)
                                                            .addContainerGap())
                                                .addGroup(
                                                    groupLayout.createSequentialGroup()
                                                            .addComponent(
                                                                panel,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(ComponentPlacement.RELATED, 816, Short.MAX_VALUE)
                                                            .addComponent(
                                                                panel_1,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)
                                                            .addGap(16))))
                );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(
                            groupLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(
                                        groupLayout.createParallelGroup(Alignment.LEADING)
                                                .addComponent(
                                                    panel_1,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    52,
                                                    GroupLayout.PREFERRED_SIZE)
                                                .addComponent(
                                                    panel,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    GroupLayout.PREFERRED_SIZE))
                                    .addGap(18)
                                    .addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                                    .addContainerGap())
                );

        JScrollPane scrollPane = new JScrollPane();
        splitPane.setLeftComponent(scrollPane);
        scrollPane.setViewportBorder(new LineBorder(Color.LIGHT_GRAY));

        _elementChildrenList = new JList();
        scrollPane.setViewportView(_elementChildrenList);

        JScrollPane scrollPane_1 = new JScrollPane();
        splitPane.setRightComponent(scrollPane_1);
        scrollPane_1.setViewportBorder(new LineBorder(new Color(192, 192, 192)));

        _tableHeaderLabel = new JLabel("test");
        _tableHeaderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        _tableHeaderLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scrollPane_1.setColumnHeaderView(_tableHeaderLabel);

        _detailTable = new JTable();
        _detailTable.setShowGrid(false);
        _detailTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        _detailTable.setFillsViewportHeight(true);
        _detailTable.setRowHeight(20);
        scrollPane_1.setViewportView(_detailTable);
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

    public JLabel getTableHeaderLabel() {
        return _tableHeaderLabel;
    }

    public JLabel getElementLabel()
    {
        return _elementLabel;
    }
}
