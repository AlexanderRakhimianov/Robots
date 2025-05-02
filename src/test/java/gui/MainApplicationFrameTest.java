package gui;

import log.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MainApplicationFrameTest {

    @Test
    void testLoadProfileWhenFileNotExists() {
        try (MockedStatic<JOptionPane> mockedJOptionPane = mockStatic(JOptionPane.class)) {
            MainApplicationFrame frame = mock(MainApplicationFrame.class, CALLS_REAL_METHODS);

            // Мокаем метод getProfileFilePath()
            when(frame.getProfileFilePath()).thenReturn("/non/existing/path/profile.dat");

            WindowProfile result = frame.loadProfile();

            assertNull(result);
            mockedJOptionPane.verifyNoInteractions();
        }
    }

    @Test
    void testLoadProfileWhenUserRejects(@TempDir File tempDir) throws Exception {
        File profileFile = new File(tempDir, "robots_window_profile.dat");
        profileFile.createNewFile();

        try (MockedStatic<JOptionPane> mockedJOptionPane = mockStatic(JOptionPane.class)) {

            mockedJOptionPane.when(() -> JOptionPane.showConfirmDialog(
                    any(), any(), any(), anyInt())).thenReturn(JOptionPane.NO_OPTION);

            MainApplicationFrame frame = mock(MainApplicationFrame.class, CALLS_REAL_METHODS);
            MainApplicationFrame.setProfileFilePath(profileFile.toString());

            WindowProfile result = frame.loadProfile();

            assertNull(result);
            mockedJOptionPane.verify(() -> JOptionPane.showConfirmDialog(
                    any(), any(), any(), anyInt()));
        }
    }

    @Test
    void testShouldCreateWindowWhenProfileIsNull() {
        MainApplicationFrame frame = mock(MainApplicationFrame.class, CALLS_REAL_METHODS);

        boolean result = frame.shouldCreateWindow(null, "LogWindow");
        assertTrue(result);
    }

    @Test
    void testShouldCreateWindowWhenWindowTypeExists() {
        WindowProfile profile = new WindowProfile();
        profile.addWindowState(new WindowState("LogWindow", 0, 0, 100, 100, true, false));

        MainApplicationFrame frame = mock(MainApplicationFrame.class, CALLS_REAL_METHODS);

        boolean result = frame.shouldCreateWindow(profile, "LogWindow");
        assertTrue(result);
    }

    @Test
    void testShouldCreateWindowWhenWindowTypeNotExists() {
        WindowProfile profile = new WindowProfile();
        profile.addWindowState(new WindowState("GameWindow", 0, 0, 100, 100, true, false));

        MainApplicationFrame frame = mock(MainApplicationFrame.class, CALLS_REAL_METHODS);

        boolean result = frame.shouldCreateWindow(profile, "LogWindow");
        assertFalse(result);
    }

    @Test
    void testRestoreWindowProfileWithNullProfile() {
        MainApplicationFrame frame = mock(MainApplicationFrame.class, CALLS_REAL_METHODS);

        JRootPane mockRootPane = mock(JRootPane.class);
        JDesktopPane mockDesktopPane = mock(JDesktopPane.class);

        when(frame.getRootPane()).thenReturn(mockRootPane);
        when(mockRootPane.getContentPane()).thenReturn(mockDesktopPane);

        frame.restoreWindowProfile(null);

        verify(mockDesktopPane, never()).getAllFrames();
    }

    @Test
    void testRestoreWindowProfileWithValidProfile() {
        WindowProfile profile = new WindowProfile();
        profile.addWindowState(new WindowState("LogWindow", 100, 200, 300, 400, true, false));
        profile.addWindowState(new WindowState("GameWindow", 50, 60, 700, 800, false, true));

        MainApplicationFrame frame = new MainApplicationFrame();
        frame.setVisible(true);

        // Получаем ссылки на созданные окна
        JInternalFrame[] frames = frame.getDesktopPane().getAllFrames();
        LogWindow logWindow = (LogWindow) Arrays.stream(frames)
                .filter(f -> f instanceof LogWindow)
                .findFirst()
                .orElseThrow();
        GameWindow gameWindow = (GameWindow)Arrays.stream(frames)
                .filter(f -> f instanceof GameWindow)
                .findFirst()
                .orElseThrow();

        frame.restoreWindowProfile(profile);

        assertEquals(100, logWindow.getX());
        assertEquals(200, logWindow.getY());
        assertEquals(300, logWindow.getWidth());
        assertEquals(400, logWindow.getHeight());
        assertTrue(logWindow.isVisible());
        assertFalse(logWindow.isIcon());

        assertEquals(50, gameWindow.getX());
        assertEquals(60, gameWindow.getY());
        assertEquals(700, gameWindow.getWidth());
        assertEquals(800, gameWindow.getHeight());
        assertFalse(gameWindow.isVisible());
        assertTrue(gameWindow.isIcon());
    }

    @Test
    void testSaveWindowProfile(@TempDir File tempDir) throws Exception {
        File profileFile = new File(tempDir, "robots_window_profile.dat");
        MainApplicationFrame.setProfileFilePath(profileFile.toString());

        MainApplicationFrame frame = new MainApplicationFrame();

        for (JInternalFrame frm : frame.getDesktopPane().getAllFrames()) {
            frm.dispose();
        }

        // Добавляем тестовое окно
        LogWindow testFrame = new LogWindow(Logger.getDefaultLogSource());
        testFrame.setBounds(10, 20, 300, 400);
        testFrame.setVisible(true);
        frame.getDesktopPane().add(testFrame);

        frame.saveWindowProfile();

        assertTrue(profileFile.exists());

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(profileFile))) {
            WindowProfile savedProfile = (WindowProfile) ois.readObject();
            assertEquals(1, savedProfile.getWindowStates().size());

            WindowState savedState = savedProfile.getWindowStates().get(0);
            assertEquals("LogWindow", savedState.getWindowType());
            assertEquals(10, savedState.getX());
            assertEquals(20, savedState.getY());
            assertEquals(300, savedState.getWidth());
            assertEquals(400, savedState.getHeight());
            assertTrue(savedState.isVisible());
            assertFalse(savedState.isIconified());
        }
    }

    @Test
    void testSaveWindowProfileWithIOException(@TempDir File tempDir) {
        // Создаем директорию без прав на запись
        File dir = new File(tempDir, "readonly");
        dir.mkdir();
        dir.setReadOnly();

        MainApplicationFrame.setProfileFilePath(new File(dir, "profile.dat").toString());

        MainApplicationFrame frame = spy(new MainApplicationFrame());

        JDesktopPane mockDesktopPane = mock(JDesktopPane.class);
        doReturn(mockDesktopPane).when(frame).getContentPane();
        when(mockDesktopPane.getAllFrames()).thenReturn(new JInternalFrame[0]);

        // Проверяем, что метод не бросает исключение при ошибке записи
        assertDoesNotThrow(frame::saveWindowProfile);
    }
}