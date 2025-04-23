package gui;

import java.awt.*;

public class Enemy extends BaseRobot {

    public Enemy(double startX, double startY) {
        super(startX, startY, Color.BLUE, 0.3, 0.003);
    }

    public Color getColor() { return color; }

    public void moveTowards(double targetX, double targetY, double duration, Dimension size) {
        double distance = distance(targetX, targetY, positionX, positionY);
        if (distance < 0.5) {
            return; // Достигли цели
        }

        double velocity = maxVelocity;
        double angleToTarget = angleTo(positionX, positionY, targetX, targetY);
        double angularVelocity = 0;

        if (angleToTarget > direction) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < direction) {
            angularVelocity = -maxAngularVelocity;
        }

        updatePosition(velocity, angularVelocity, duration, size);
    }

    void updatePosition(double velocity, double angularVelocity, double duration, Dimension size) {
        velocity = Math.max(-maxVelocity, Math.min(velocity, maxVelocity));
        angularVelocity = Math.max(-maxAngularVelocity,
                Math.min(angularVelocity, maxAngularVelocity));

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