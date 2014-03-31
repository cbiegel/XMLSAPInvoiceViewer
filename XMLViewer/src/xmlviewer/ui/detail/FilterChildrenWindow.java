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


/**
 * @author cbiegel
 *         This class serves as the UI component of the FilterChildren component
 */
@SuppressWarnings("serial")
public class FilterChildrenWindow extends JDialog {

    private CheckBoxList _attributesCheckBoxList;
    private CheckBoxList _childrenCheckBoxList;
    private JLabel _parentNameLabel;
    private JButton _okButton;
    private JButton _cancelButton;
    private JButton _selectAllChildrenButton;
    private JButton _deselectAllChildrenButton;
    private JButton _selectAllAttributesButton;
    private JButton _deselectAllAttributesButton;

    /**
     * Create the dialog.
     */
    public FilterChildrenWindow(JFrame owner) {
        super(owner);
        setTitle("Filter children");
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        final JPanel contentPanel = new JPanel();

        int width = 630;
        int height = 380;
        int x = (owner == null) ? 0 : ((owner.getWidth() / 2) - (width / 2) + owner.getX());
        int y = (owner == null) ? 0 : ((owner.getHeight() / 2) - (height / 2) + owner.getY());

        setBounds(x, y, width, height);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JSeparator separator = new JSeparator();

        JLabel lblNewLabel = new JLabel("Filter children:");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JLabel lblIncludeAttributes = new JLabel("Include attributes:");
        lblIncludeAttributes.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane();

        JScrollPane scrollPane_1 = new JScrollPane();

        JPanel selectPanel_children = new JPanel();

        JPanel selectPanel_attributes = new JPanel();
        selectPanel_attributes.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        _selectAllAttributesButton = new JButton("Select All");
        selectPanel_attributes.add(_selectAllAttributesButton);

        _deselectAllAttributesButton = new JButton("Deselect All");
        selectPanel_attributes.add(_deselectAllAttributesButton);
        GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
        gl_contentPanel.setHorizontalGroup(
                gl_contentPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(
                            gl_contentPanel.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(
                                        gl_contentPanel.createParallelGroup(Alignment.LEADING)
                                                .addComponent(separator, GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                                                .addGroup(
                                                    gl_contentPanel.createSequentialGroup()
                                                            .addGroup(
                                                                gl_contentPanel.createParallelGroup(Alignment.LEADING)
                                                                        .addComponent(
                                                                            selectPanel_children,
                                                                            GroupLayout.DEFAULT_SIZE,
                                                                            GroupLayout.DEFAULT_SIZE,
                                                                            Short.MAX_VALUE)
                                                                        .addComponent(scrollPane))
                                                            .addGap(0)
                                                            .addGroup(
                                                                gl_contentPanel.createParallelGroup(Alignment.TRAILING)
                                                                        .addGroup(
                                                                            gl_contentPanel.createSequentialGroup()
                                                                                    .addGap(52)
                                                                                    .addComponent(
                                                                                        scrollPane_1,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        274,
                                                                                        Short.MAX_VALUE))
                                                                        .addGroup(
                                                                            gl_contentPanel.createSequentialGroup()
                                                                                    .addGap(49)
                                                                                    .addComponent(
                                                                                        selectPanel_attributes,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        277,
                                                                                        Short.MAX_VALUE)))))
                                    .addContainerGap())
                        .addGroup(gl_contentPanel.createSequentialGroup()
                                .addGap(90)
                                .addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                .addGap(211)
                                .addComponent(lblIncludeAttributes, GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
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
                                        gl_contentPanel.createParallelGroup(Alignment.BASELINE, false)
                                                .addComponent(
                                                    lblIncludeAttributes,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    15,
                                                    GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblNewLabel))
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
                                            .addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                                            .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE))
                                    .addPreferredGap(ComponentPlacement.UNRELATED)
                                    .addGroup(
                                        gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
                                                .addComponent(selectPanel_children, 0, 0, Short.MAX_VALUE)
                                                .addComponent(
                                                    selectPanel_attributes,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    28,
                                                    Short.MAX_VALUE)))
                );
        selectPanel_children.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        _selectAllChildrenButton = new JButton("Select All");
        selectPanel_children.add(_selectAllChildrenButton);

        _deselectAllChildrenButton = new JButton("Deselect All");
        selectPanel_children.add(_deselectAllChildrenButton);

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

    public JButton getUISelectAllChildrenButton() {
        return _selectAllChildrenButton;
    }

    public JButton getUISelectAllAttributesButton() {
        return _selectAllAttributesButton;
    }

    public JButton getUIDeselectAllChildrenButton() {
        return _deselectAllChildrenButton;
    }

    public JButton getUIDeselectAllAttributesButton() {
        return _deselectAllAttributesButton;
    }
}
