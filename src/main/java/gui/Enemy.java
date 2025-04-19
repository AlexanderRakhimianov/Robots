package gui;

import java.awt.*;

public class Enemy {
    private static final double ENEMY_MAX_VELOCITY = 0.18;
    private static final double ENEMY_MAX_ANGULAR_VELOCITY = 0.0018;

    volatile double positionX;
    volatile double positionY;
    volatile double direction;
    private final Color color;

    public Enemy(double startX, double startY, Color color) {
        this.positionX = startX;
        this.positionY = startY;
        this.direction = 0;
        this.color = color;
    }
    public Color getColor() { return color; }

    public void moveTowards(double targetX, double targetY, double duration, Dimension size) {
        double distance = distance(targetX, targetY, positionX, positionY);
        if (distance < 0.5) {
            return; // Достигли цели
        }

        double velocity = ENEMY_MAX_VELOCITY;
        double angleToTarget = angleTo(positionX, positionY, targetX, targetY);
        double angularVelocity = 0;

        if (angleToTarget > direction) {
            angularVelocity = ENEMY_MAX_ANGULAR_VELOCITY;
        }
        if (angleToTarget < direction) {
            angularVelocity = -ENEMY_MAX_ANGULAR_VELOCITY;
        }

        updatePosition(velocity, angularVelocity, duration, size);
    }

    void updatePosition(double velocity, double angularVelocity, double duration, Dimension size) {
        velocity = Math.max(-ENEMY_MAX_VELOCITY, Math.min(velocity, ENEMY_MAX_VELOCITY));
        angularVelocity = Math.max(-ENEMY_MAX_ANGULAR_VELOCITY,
                Math.min(angularVelocity, ENEMY_MAX_ANGULAR_VELOCITY));

        double newX = positionX + velocity * duration * Math.cos(direction);
        double newY = positionY + velocity * duration * Math.sin(direction);

        int fieldWidth = size.width * 2, fieldHeight = size.height * 2;
        if (newX < 0) newX = fieldWidth;
        if (newY < 0) newY = fieldHeight;
        if (newX > fieldWidth) newX = 0;
        if (newY > fieldHeight) newY = 0;

        positionX = newX;
        positionY = newY;
        direction = asNormalizedRadians(direction + angularVelocity * duration);
    }

    static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double asNormalizedRadians(double angle) {
        angle %= 2*Math.PI;
        return angle < 0 ? angle + 2*Math.PI : angle;
    }
}