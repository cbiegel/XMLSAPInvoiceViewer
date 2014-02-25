package xmlviewer.ui;

import javax.swing.JCheckBox;


@SuppressWarnings("serial")
public class CustomCheckBox extends JCheckBox
{
    private boolean isHeader;
    private boolean isTrailer;

    public CustomCheckBox()
    {
        super();
        isHeader = false;
        isTrailer = false;
    }

    public CustomCheckBox(String text)
    {
        super(text);
        isHeader = false;
        isTrailer = false;
    }

    public boolean isHeader()
    {
        return isHeader;
    }

    public boolean isTrailer()
    {
        return isTrailer;
    }
}
