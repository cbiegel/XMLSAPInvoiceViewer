package xmlviewer.ui.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import xmlviewer.ui.detail.FindElementWindow;


public class FindElementTool extends Observable
{
    private static FindElementWindow _ui = null;

    public FindElementTool(JFrame parentWindow, Observer obs)
    {
        // only allow one instance of the ui
        if (_ui == null)
        {
            _ui = new FindElementWindow(parentWindow);
            addObserver(obs);
            setupListeners();
        }
        else
        {
            _ui.getFrame().requestFocus();
            _ui.getTextField().requestFocus();
        }

    }

    private void setupListeners()
    {
        JButton button = _ui.getButton();
        JTextField textfield = _ui.getTextField();

        addButtonListener(button);
        addTextFieldListener(textfield);
    }

    private void addButtonListener(final JButton button)
    {
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String searchString = _ui.getTextField().getText();
                searchElement(searchString);
            }
        });
    }

    private void addTextFieldListener(final JTextField textfield)
    {
        textfield.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                searchElement(textfield.getText());
            }
        });
    }

    private void searchElement(String searchText)
    {
        setChanged();
        notifyObservers(searchText);
    }

    public void setStatusLabel(String status)
    {
        _ui.getStatusLabel().setText(status);
    }

    public boolean isCaseSensitiveSelected()
    {
        return _ui.getCheckBoxCaseSensitive().isSelected();
    }

    public boolean isWrapSearchSelected()
    {
        return _ui.getCheckBoxWrapSearch().isSelected();
    }

    public boolean isWholeMatchSelected()
    {
        return _ui.getCheckBoxWholeMatch().isSelected();
    }

    public boolean isSearchAttributesSelected()
    {
        return _ui.getCheckBoxSearchAttributes().isSelected();
    }
}
