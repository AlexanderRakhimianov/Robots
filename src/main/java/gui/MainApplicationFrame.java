package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.*;

import javax.swing.*;

import log.Logger;


public class MainApplicationFrame extends BaseFrame {
    private static String PROFILE_FILE = System.getProperty("user.home") + "/robots_window_profile.dat";
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        super("Robots");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setContentPane(desktopPane);

        WindowProfile profile = loadProfile();

        if (shouldCreateWindow(profile, "LogWindow")) {
            LogWindow logWindow = createLogWindow();
            addWindow(logWindow);
        }

        if (shouldCreateWindow(profile, "GameWindow")) {
            GameWindow gameWindow = new GameWindow();
            gameWindow.setSize(screenSize.width, screenSize.height - 160);
            addWindow(gameWindow);
        }

        setJMenuBar(generateMenuBar());

        restoreWindowProfile(profile);
    }

    protected JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    protected String getProfileFilePath() {
        return PROFILE_FILE;
    }

    public static void setProfileFilePath(String path) {
        PROFILE_FILE = path;
    }

    protected WindowProfile loadProfile() {
        File profileFile = new File(getProfileFilePath());
        if (!profileFile.exists()) return null;

        int response = JOptionPane.showConfirmDialog(
                this,
                "Обнаружен сохранённый профиль окон. Восстановить?",
                "Восстановление профиля",
                JOptionPane.YES_NO_OPTION
        );

        if (response != JOptionPane.YES_OPTION) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PROFILE_FILE))) {
            return (WindowProfile) ois.readObject();
        } catch (Exception e) {
            Logger.debug("Ошибка загрузки профиля: " + e.getMessage());
            return null;
        }
    }

    protected boolean shouldCreateWindow(WindowProfile profile, String windowType) {
        if (profile == null) return true;

        for (WindowState state : profile.getWindowStates()) {
            if (state.getWindowType().equals(windowType)) {
                return true;
            }
        }
        return false;
    }

    protected void restoreWindowProfile(WindowProfile profile) {
        if (profile == null) return;

        for (WindowState state : profile.getWindowStates()) {
            for (JInternalFrame frame : desktopPane.getAllFrames()) {
                if (frame.getClass().getSimpleName().equals(state.getWindowType())) {
                    frame.setLocation(state.getX(), state.getY());
                    frame.setSize(state.getWidth(), state.getHeight());
                    frame.setVisible(state.isVisible());
                    try {
                        frame.setIcon(state.isIconified());
                    }
                    catch (PropertyVetoException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
            }
        }
    }

    protected void saveWindowProfile() {
        WindowProfile profile = new WindowProfile();

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            WindowState state = new WindowState(
                    frame.getClass().getSimpleName(),
                    frame.getX(),
                    frame.getY(),
                    frame.getWidth(),
                    frame.getHeight(),
                    frame.isVisible(),
                    frame.isIcon()
            );
            profile.addWindowState(state);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROFILE_FILE))) {
            oos.writeObject(profile);
            System.out.println("Профиль сохранён");
        } catch (IOException e) {
            System.out.println("Не удалось сохранить профиль окон: " + e.getMessage());
        }
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = createJMenu("Режим отображения",
                "Управление режимом отображения приложения", KeyEvent.VK_V);

        JMenuItem systemLookAndFeel = createSystemLookAndFeel();
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = createCrossplatformLookAndFeel();
        lookAndFeelMenu.add(crossplatformLookAndFeel);

        JMenu testMenu = createJMenu("Тесты", "Тестовые команды", KeyEvent.VK_T);

        JMenuItem addLogMessageItem = createAddLogMessageItem();
        testMenu.add(addLogMessageItem);

        JMenu MainRobotsMenu = createJMenu("RobotsProgram", "Основное меню приложения", KeyEvent.VK_E);
        JMenuItem exitItem = createExitItem();
        MainRobotsMenu.add(exitItem);

        menuBar.add(MainRobotsMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);

        return menuBar;
    }

    private JMenu createJMenu(String name, String description, int mnemonic) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(mnemonic);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }

    private JMenuItem createSystemLookAndFeel() {
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return systemLookAndFeel;
    }

    private JMenuItem createCrossplatformLookAndFeel() {
        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return crossplatformLookAndFeel;
    }

    private JMenuItem createAddLogMessageItem() {
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        return addLogMessageItem;
    }

    private JMenuItem createExitItem() {
        JMenuItem ExitItem = new JMenuItem("Выйти", KeyEvent.VK_S);
        ExitItem.addActionListener((event) -> {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        return ExitItem;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
    }
}