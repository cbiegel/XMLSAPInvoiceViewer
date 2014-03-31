package xmlviewer.ui.detail;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import xmlviewer.ui.main.MainWindow;


public class FindElementWindow
{

    private JFrame _frame;
    private JPanel _contentPane;
    private JTextField _textField;
    private JButton _findButton;

    private final int HEIGHT = 170;
    private final int WIDTH = 460;
    private final int OFFSET_X = 200;
    private final int OFFSET_Y = 100;
    private JPanel checkBoxPanel;
    private JCheckBox _checkBoxCaseSensitive;
    private JCheckBox _checkBoxWrapSearch;
    private JPanel statusPanel;
    private JLabel _statusLabel;
    private JCheckBox _checkBoxWholeMatch;
    private JCheckBox _checkBoxSearchAttributes;

    /**
     * Create the frame.
     */
    public FindElementWindow(JFrame parent) {
        _frame = new JFrame("Find Element");
        _frame.setAlwaysOnTop(true);
        _frame.setResizable(false);
        _frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        _frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
            (getClass().getResource(MainWindow.ICON_FIND_ELEMENT_PATH))));

        int parentY = parent.getY();
        int parentWidth = parent.getWidth();

        _frame.setBounds((parentWidth - WIDTH - OFFSET_X), (parentY + OFFSET_Y), WIDTH, HEIGHT);

        _contentPane = new JPanel();
        _contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        _frame.setContentPane(_contentPane);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setBounds(5, 5, 339, 53);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(354, 34, 92, 22);
        buttonPanel.setLayout(new BorderLayout(0, 0));

        _findButton = new JButton("Find");
        _findButton.setFocusPainted(false);
        buttonPanel.add(_findButton, BorderLayout.CENTER);

        JLabel lblElementName = new JLabel("Element Name:");
        lblElementName.setFont(new Font("Tahoma", Font.PLAIN, 12));

        _textField = new JTextField();
        _textField.setColumns(10);
        GroupLayout gl_textFieldPanel = new GroupLayout(textFieldPanel);
        gl_textFieldPanel.setHorizontalGroup(
                gl_textFieldPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_textFieldPanel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_textFieldPanel.createParallelGroup(Alignment.LEADING)
                                        .addComponent(lblElementName)
                                        .addComponent(_textField, GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE))
                                .addContainerGap())
                );
        gl_textFieldPanel.setVerticalGroup(
                gl_textFieldPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(
                            gl_textFieldPanel.createSequentialGroup()
                                    .addGap(5)
                                    .addComponent(lblElementName)
                                    .addPreferredGap(ComponentPlacement.UNRELATED)
                                    .addComponent(
                                        _textField,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(21, Short.MAX_VALUE))
                );
        textFieldPanel.setLayout(gl_textFieldPanel);

        checkBoxPanel = new JPanel();
        checkBoxPanel.setBounds(5, 64, 228, 67);
        checkBoxPanel.setBorder(new TitledBorder(null, "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        statusPanel = new JPanel();
        statusPanel.setBounds(243, 64, 201, 67);
        statusPanel.setBorder(null);

        _statusLabel = new JLabel("");
        _statusLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        GroupLayout gl_statusPanel = new GroupLayout(statusPanel);
        gl_statusPanel.setHorizontalGroup(
                gl_statusPanel.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_statusPanel.createSequentialGroup()
                                .addContainerGap(33, Short.MAX_VALUE)
                                .addComponent(_statusLabel, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                );
        gl_statusPanel.setVerticalGroup(
                gl_statusPanel.createParallelGroup(Alignment.TRAILING)
                        .addGroup(Alignment.LEADING, gl_statusPanel.createSequentialGroup()
                                .addGap(29)
                                .addComponent(_statusLabel, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(27, Short.MAX_VALUE))
                );
        statusPanel.setLayout(gl_statusPanel);

        _checkBoxCaseSensitive = new JCheckBox("Case Sensitive");
        _checkBoxCaseSensitive.setFocusPainted(false);

        _checkBoxWrapSearch = new JCheckBox("Wrap Search");
        _checkBoxWrapSearch.setFocusPainted(false);
        _checkBoxWrapSearch.setSelected(true);

        _checkBoxWholeMatch = new JCheckBox("Whole Match");
        _checkBoxWholeMatch.setFocusPainted(false);

        _checkBoxSearchAttributes = new JCheckBox("Search Attributes");
        _checkBoxSearchAttributes.setFocusPainted(false);

        GroupLayout gl_checkBoxPanel = new GroupLayout(checkBoxPanel);
        gl_checkBoxPanel.setHorizontalGroup(
                gl_checkBoxPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_checkBoxPanel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_checkBoxPanel.createParallelGroup(Alignment.LEADING)
                                        .addComponent(_checkBoxCaseSensitive)
                                        .addComponent(_checkBoxWholeMatch))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(gl_checkBoxPanel.createParallelGroup(Alignment.LEADING)
                                        .addComponent(_checkBoxWrapSearch)
                                        .addComponent(_checkBoxSearchAttributes))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
        gl_checkBoxPanel.setVerticalGroup(
                gl_checkBoxPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_checkBoxPanel.createSequentialGroup()
                                .addGroup(gl_checkBoxPanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(_checkBoxCaseSensitive)
                                        .addComponent(_checkBoxWrapSearch))
                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(gl_checkBoxPanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(_checkBoxSearchAttributes)
                                        .addComponent(_checkBoxWholeMatch)))
                );
        checkBoxPanel.setLayout(gl_checkBoxPanel);
        _contentPane.setLayout(null);
        _contentPane.add(textFieldPanel);
        _contentPane.add(buttonPanel);
        _contentPane.add(checkBoxPanel);
        _contentPane.add(statusPanel);

        _frame.setVisible(true);
    }

    public JFrame getFrame() {
        return _frame;
    }

    public JTextField getTextField() {
        return _textField;
    }

    public JButton getButton() {
        return _findButton;
    }

    public JCheckBox getCheckBoxCaseSensitive() {
        return _checkBoxCaseSensitive;
    }

    public JCheckBox getCheckBoxWrapSearch() {
        return _checkBoxWrapSearch;
    }

    public JCheckBox getCheckBoxWholeMatch() {
        return _checkBoxWholeMatch;
    }

    public JCheckBox getCheckBoxSearchAttributes() {
        return _checkBoxSearchAttributes;
    }

    public JLabel getStatusLabel() {
        return _statusLabel;
    }
}
