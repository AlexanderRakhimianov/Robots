package gui;

import java.awt.Color;

public class BaseRobot {
    protected volatile double positionX;
    protected volatile double positionY;
    protected volatile double direction;

    protected final double maxVelocity;
    protected final double maxAngularVelocity;

    protected final Color color;

    protected final int width;
    protected final int height;
    protected final int diameter;

    public BaseRobot(double startX, double startY, Color color, double maxVelocity, double maxAngularVelocity) {
        this.positionX = startX;
        this.positionY = startY;
        this.direction = 0;
        this.color = color;
        this.maxVelocity = maxVelocity;
        this.maxAngularVelocity = maxAngularVelocity;
        this.width = 40;
        this.height = 120;
        this.diameter = 20;
    }
}