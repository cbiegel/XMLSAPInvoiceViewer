package xmlviewer.ui.tools;

import java.util.prefs.Preferences;


public class UserPreferences
{
    private static final Preferences _prefs = Preferences.userRoot().node("xmlviewer/ui");

    private static final String KEY_FRAME_XPOS = "FRAME_XPOS";
    private static final String KEY_FRAME_YPOS = "FRAME_YPOS";
    private static final String KEY_FRAME_WIDTH = "FRAME_WIDTH";
    private static final String KEY_FRAME_HEIGHT = "FRAME_HEIGHT";
    private static final String KEY_SAVE_WINDOW_POS = "SAVE_WINDOW_POS";

    // loading properties

    public static int loadWindowPositionX()
    {
        return _prefs.getInt(KEY_FRAME_XPOS, 0);
    }

    public static int loadWindowPositionY()
    {
        return _prefs.getInt(KEY_FRAME_YPOS, 0);
    }

    public static int loadWindowWidth()
    {
        return _prefs.getInt(KEY_FRAME_WIDTH, 0);
    }

    public static int loadWindowHeight()
    {
        return _prefs.getInt(KEY_FRAME_HEIGHT, 0);
    }

    public static boolean loadSaveWindowPositionProperty()
    {
        return _prefs.getBoolean(KEY_SAVE_WINDOW_POS, false);
    }

    // saving properties

    public static void saveWindowPositionX(int value)
    {
        _prefs.putInt(KEY_FRAME_XPOS, value);
    }

    public static void saveWindowPositionY(int value)
    {
        _prefs.putInt(KEY_FRAME_YPOS, value);
    }

    public static void saveWindowWidth(int value)
    {
        _prefs.putInt(KEY_FRAME_WIDTH, value);
    }

    public static void saveWindowHeight(int value)
    {
        _prefs.putInt(KEY_FRAME_HEIGHT, value);
    }

    public static void saveSaveWindowPositionProperty(boolean value)
    {
        _prefs.putBoolean(KEY_SAVE_WINDOW_POS, value);
    }
}
