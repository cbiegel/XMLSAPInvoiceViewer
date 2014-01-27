package tests.ui.tools;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import xmlviewer.ui.tools.UserPreferences;


public class UserPreferencesTest {

    @Before
    public void setUp()
            throws Exception {
    }

    @Test
    public void testSaveLoadWindowPosisionX()
    {
        UserPreferences.saveWindowPositionX(1);
        assertEquals(1, UserPreferences.loadWindowPositionX());
        UserPreferences.saveWindowPositionX(0);
        assertEquals(0, UserPreferences.loadWindowPositionX());
    }

    @Test
    public void testSaveLoadWindowPosisionY()
    {
        UserPreferences.saveWindowPositionY(1);
        assertEquals(1, UserPreferences.loadWindowPositionY());
        UserPreferences.saveWindowPositionY(0);
        assertEquals(0, UserPreferences.loadWindowPositionY());
    }

    @Test
    public void testSaveLoadWindowHeight()
    {
        UserPreferences.saveWindowHeight(0);
        assertEquals(0, UserPreferences.loadWindowHeight());
        UserPreferences.saveWindowHeight(500);
        assertEquals(500, UserPreferences.loadWindowHeight());
    }

    @Test
    public void testSaveLoadWindowWidth()
    {
        UserPreferences.saveWindowWidth(0);
        assertEquals(0, UserPreferences.loadWindowWidth());
        UserPreferences.saveWindowWidth(400);
        assertEquals(400, UserPreferences.loadWindowWidth());
    }

    @Test
    public void testSaveLoadSaveTreeStateProperty()
    {
        UserPreferences.saveSaveTreeStateProperty(false);
        assertEquals(false, UserPreferences.loadSaveTreeStateProperty());
        UserPreferences.saveSaveTreeStateProperty(true);
        assertEquals(true, UserPreferences.loadSaveTreeStateProperty());
    }

    @Test
    public void testSaveLoadSaveWindowPositionProperty()
    {
        UserPreferences.saveSaveWindowPositionProperty(false);
        assertEquals(false, UserPreferences.loadSaveWindowPositionProperty());
        UserPreferences.saveSaveWindowPositionProperty(true);
        assertEquals(true, UserPreferences.loadSaveWindowPositionProperty());
    }
}
