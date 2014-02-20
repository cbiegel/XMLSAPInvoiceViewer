package xmlviewer.ui.detail;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;


public class FilterChildren extends JDialog {

    private CheckBoxList _attributesCheckBoxList;
    private CheckBoxList _childrenCheckBoxList;
    private JLabel _parentNameLabel;
    private JButton _okButton;
    private JButton _cancelButton;

    /**
     * Create the dialog.
     */
    public FilterChildren(JFrame owner) {
        super(owner);
        setTitle("Filter children");
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        final JPanel contentPanel = new JPanel();

        int width = 600;
        int height = 350;
        int x = ((owner.getWidth() / 2) - (width / 2) + owner.getX());
        int y = ((owner.getHeight() / 2) - (height / 2) + owner.getY());

        setBounds(x, y, width, height);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JSeparator separator = new JSeparator();

        JLabel lblNewLabel = new JLabel("Include children:");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JLabel lblIncludeAttributes = new JLabel("Include attributes:");
        lblIncludeAttributes.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane();

        JScrollPane scrollPane_1 = new JScrollPane();
        GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
        gl_contentPanel.setHorizontalGroup(
                gl_contentPanel.createParallelGroup(Alignment.TRAILING)
                        .addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_contentPanel.createSequentialGroup()
                                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                                                .addGap(52)
                                                .addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
                                        .addComponent(separator, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE))
                                .addContainerGap())
                        .addGroup(gl_contentPanel.createSequentialGroup()
                                .addGap(90)
                                .addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                .addGap(211)
                                .addComponent(lblIncludeAttributes, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                .addGap(72))
                );
        gl_contentPanel.setVerticalGroup(
                gl_contentPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(
                            gl_contentPanel.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(
                                        panel,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(
                                        separator,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addGroup(
                                        gl_contentPanel.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(
                                                    lblIncludeAttributes,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    15,
                                                    Short.MAX_VALUE)
                                                .addComponent(
                                                    lblNewLabel,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    Short.MAX_VALUE))
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
                                            .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                            .addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
                                    .addGap(10))
                );

        _attributesCheckBoxList = new CheckBoxList();
        scrollPane_1.setViewportView(_attributesCheckBoxList);

        _childrenCheckBoxList = new CheckBoxList();
        scrollPane.setViewportView(_childrenCheckBoxList);

        JLabel lblFilteringChildrenFor = new JLabel("Filtering children for");
        lblFilteringChildrenFor.setFont(new Font("Tahoma", Font.PLAIN, 12));
        _parentNameLabel = new JLabel("");
        _parentNameLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addComponent(lblFilteringChildrenFor)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(_parentNameLabel)
                                .addContainerGap(68, Short.MAX_VALUE))
                );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblFilteringChildrenFor)
                                        .addComponent(_parentNameLabel))
                                .addContainerGap(12, Short.MAX_VALUE))
                );
        panel.setLayout(gl_panel);
        contentPanel.setLayout(gl_contentPanel);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                _okButton = new JButton("OK");
                _okButton.setActionCommand("OK");
                buttonPane.add(_okButton);
                getRootPane().setDefaultButton(_okButton);
            }
            {
                _cancelButton = new JButton("Cancel");
                _cancelButton.setActionCommand("Cancel");
                buttonPane.add(_cancelButton);
            }
        }
    }

    public CheckBoxList getUIAttributesCheckBoxList() {
        return _attributesCheckBoxList;
    }

    public CheckBoxList getUIChildrenCheckBoxList() {
        return _childrenCheckBoxList;
    }

    public JLabel getUIParentNameLabel() {
        return _parentNameLabel;
    }

    public JButton getUIOkButton() {
        return _okButton;
    }

    public JButton getUICancelButton() {
        return _cancelButton;
    }
}
