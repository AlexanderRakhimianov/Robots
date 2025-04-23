package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import java.util.Timer;
import javax.swing.*;

public class GameVisualizer extends JPanel {
    private final Timer timer;
    protected final Robot robot;
    private final List<Enemy> enemies = new ArrayList<>();
    private BufferedImage backgroundImage;

    public GameVisualizer() {
        this.timer = new Timer("events generator", true);
        this.robot = new Robot(this);

        setDoubleBuffered(true);
        setFocusable(true);
        loadResources();
        initTimerTasks();
        SwingUtilities.invokeLater(this::initializeEnemies);
    }

    private void initTimerTasks() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 33);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 16);
    }

    private void initializeEnemies() {
        int width = getWidth() * 2;
        int height = getHeight() * 2;

        enemies.clear();
        enemies.add(new Enemy(width * 0.2, height * 0.2));
        enemies.add(new Enemy(width * 0.8, height * 0.3));
        enemies.add(new Enemy(width * 0.5, height * 0.7));
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    private void onModelUpdateEvent() {
        Dimension size = getSize();
        for (Enemy enemy : enemies) {
            enemy.moveTowards(robot.positionX, robot.positionY, 10, size);
        }
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
            drawBaseRobot(g2d, enemy);
        }

        g2d.setTransform(originalTransform);

        drawBaseRobot(g2d, robot);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawBaseRobot(Graphics2D g, BaseRobot baserobot) {
        AffineTransform t = AffineTransform.getRotateInstance(
                baserobot.direction,
                round(baserobot.positionX),
                round(baserobot.positionY)
        );
        g.setTransform(t);
        g.setColor(baserobot.color);
        fillOval(g, round(baserobot.positionX), round(baserobot.positionY), baserobot.height, baserobot.width);
        g.setColor(Color.BLACK);
        drawOval(g, round(baserobot.positionX), round(baserobot.positionY), baserobot.height, baserobot.width);
        g.setColor(Color.WHITE);
        fillOval(g, round(baserobot.positionX) + baserobot.width, round(baserobot.positionY),
                baserobot.diameter, baserobot.diameter);
        g.setColor(Color.BLACK);
        drawOval(g, round(baserobot.positionX) + baserobot.width, round(baserobot.positionY),
                baserobot.diameter, baserobot.diameter);
    }

    private static int round(double value) {
        return (int)(value + 0.5);
    }
}