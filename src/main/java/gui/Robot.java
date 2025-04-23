package gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Robot extends BaseRobot {
    private final Timer timer;
    private final GameVisualizer visualizer;

    protected boolean wPressed = false;
    protected boolean aPressed = false;
    protected boolean sPressed = false;
    protected boolean dPressed = false;

    public Robot(GameVisualizer visualizer) {
        super(100, 100, Color.MAGENTA, 0.4, 0.004);
        this.visualizer = visualizer;
        this.timer = new Timer("robot controller", true);

        initKeyListeners();
        startControlLoop();
    }

    private void initKeyListeners() {
        visualizer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), true);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), false);
            }
        });
    }

    private void startControlLoop() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handleKeyboardControl();
            }
        }, 0, 10);
    }

    void handleKeyPress(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_W: wPressed = pressed; break;
            case KeyEvent.VK_A: aPressed = pressed; break;
            case KeyEvent.VK_S: sPressed = pressed; break;
            case KeyEvent.VK_D: dPressed = pressed; break;
        }
    }

    void handleKeyboardControl() {
        double velocity = 0;
        double angularVelocity = 0;

        if (wPressed) velocity = maxVelocity;
        if (sPressed) velocity = -maxVelocity;
        if (aPressed) angularVelocity = -maxAngularVelocity;
        if (dPressed) angularVelocity = maxAngularVelocity;

        if (velocity != 0 || angularVelocity != 0) {
            move(velocity, angularVelocity, 10);
        }
    }

    void move(double velocity, double angularVelocity, double duration) {
        velocity = Math.max(-maxVelocity, Math.min(velocity, maxVelocity));
        angularVelocity = Math.max(-maxAngularVelocity, Math.min(angularVelocity, maxAngularVelocity));

        double newX = positionX + velocity * duration * Math.cos(direction);
        double newY = positionY + velocity * duration * Math.sin(direction);

        Dimension size = visualizer.getSize();
        int fieldWidth = size.width * 2, fieldHeight = size.height * 2;
        if (newX < 0) newX = fieldWidth;
        if (newY < 0) newY = fieldHeight;
        if (newX > fieldWidth) newX = 0;
        if (newY > fieldHeight) newY = 0;

        positionX = newX;
        positionY = newY;
        direction = asNormalizedRadians(direction + angularVelocity * duration);
    }

    private static double asNormalizedRadians(double angle) {
        angle %= 2*Math.PI;
        return angle < 0 ? angle + 2*Math.PI : angle;
    }

}