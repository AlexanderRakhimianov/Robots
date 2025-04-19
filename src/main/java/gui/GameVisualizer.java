package gui;

import java.util.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameVisualizer extends JPanel {
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    volatile double m_robotPositionX = 100;
    volatile double m_robotPositionY = 100;
    volatile double m_robotDirection = 0;

    private static final double maxVelocity = 0.25;
    private static final double maxAngularVelocity = 0.0025;

    private BufferedImage backgroundImage;
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;

    private final List<Enemy> enemies = new ArrayList<>();

    public GameVisualizer() {
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handleKeyboardControl();
            }
        }, 0, 10);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), true);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), false);
            }
        });

        setDoubleBuffered(true);
        setFocusable(true);
        loadResources();

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
                handleKeyboardControl();
            }
        }, 0, 10);
        SwingUtilities.invokeLater(this::initializeEnemies);
    }

    private void initializeEnemies() {
        int width = getWidth() * 2;
        int height = getHeight() * 2;

        enemies.clear();
        Color default_enemy_color = Color.BLUE;
        enemies.add(new Enemy(width * 0.2, height * 0.2, default_enemy_color));
        enemies.add(new Enemy(width * 0.8, height * 0.3, default_enemy_color));
        enemies.add(new Enemy(width * 0.5, height * 0.7, default_enemy_color));
    }

    private void onModelUpdateEvent() {
        Dimension size = getSize();
        for (Enemy enemy : enemies) {
            enemy.moveTowards(m_robotPositionX, m_robotPositionY, 10, size);
        }
    }

    void handleKeyPress(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_W: wPressed = pressed; break;
            case KeyEvent.VK_A: aPressed = pressed; break;
            case KeyEvent.VK_S: sPressed = pressed; break;
            case KeyEvent.VK_D: dPressed = pressed; break;
        }
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    void handleKeyboardControl() {
        double velocity = 0;
        double angularVelocity = 0;

        if (wPressed) velocity = maxVelocity;
        if (sPressed) velocity = -maxVelocity;
        if (aPressed) angularVelocity = -maxAngularVelocity;
        if (dPressed) angularVelocity = maxAngularVelocity;

        if (velocity != 0 || angularVelocity != 0) {
            moveRobot(velocity, angularVelocity, 10);
        }
    }

    void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = Math.max(-maxVelocity, Math.min(velocity, maxVelocity));
        angularVelocity = Math.max(-maxAngularVelocity, Math.min(angularVelocity, maxAngularVelocity));

        double newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        double newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);

        Dimension size = getSize();
        int fieldWidth = size.width * 2, fieldHeight = size.height * 2;
        if (newX < 0) newX = fieldWidth;
        if (newY < 0) newY = fieldHeight;
        if (newX > fieldWidth) newX = 0;
        if (newY > fieldHeight) newY = 0;

        m_robotPositionX = newX;
        m_robotPositionY = newY;
        m_robotDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
    }

    private static double asNormalizedRadians(double angle) {
        angle %= 2*Math.PI;
        return angle < 0 ? angle + 2*Math.PI : angle;
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    private void loadResources() {
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getResource("/textures/default_background.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        AffineTransform originalTransform = g2d.getTransform();

        for (Enemy enemy : enemies) {
            drawEnemy(g2d, enemy);
        }

        g2d.setTransform(originalTransform);

        drawRobot(g2d, round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
    }

    // Новый метод для рисования врагов
    private void drawEnemy(Graphics2D g, Enemy enemy) {
        AffineTransform t = AffineTransform.getRotateInstance(
                enemy.direction,
                round(enemy.positionX),
                round(enemy.positionY)
        );
        g.setTransform(t);
        g.setColor(enemy.getColor());
        fillOval(g, round(enemy.positionX), round(enemy.positionY), 120, 40);
        g.setColor(Color.BLACK);
        drawOval(g, round(enemy.positionX), round(enemy.positionY), 120, 40);
        g.setColor(Color.WHITE);
        fillOval(g, round(enemy.positionX) + 40, round(enemy.positionY), 20, 20);
        g.setColor(Color.BLACK);
        drawOval(g, round(enemy.positionX) + 40, round(enemy.positionY), 20, 20);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = round(m_robotPositionX);
        int robotCenterY = round(m_robotPositionY);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 120, 40);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 120, 40);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 40, robotCenterY, 20, 20);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 40, robotCenterY, 20, 20);
    }

    boolean isWPressed() { return wPressed; }
    boolean isAPressed() { return aPressed; }
    boolean isSPressed() { return sPressed; }
    boolean isDPressed() { return dPressed; }
    double getRobotPositionX() { return m_robotPositionX; }
    double getRobotPositionY() { return m_robotPositionY; }
}