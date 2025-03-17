package gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Dimension;

import static org.junit.jupiter.api.Assertions.*;

class GameVisualizerTest {
    private GameVisualizer gameVisualizer;

    @BeforeEach
    void setUp() {
        gameVisualizer = new GameVisualizer();
        gameVisualizer.setSize(new Dimension(300, 300));
    }

    @Test
    void testMoveRobotBoundaryLimits() {
        gameVisualizer.moveRobot(10, 0, 1000); // Движение вперёд, выход за границы

        assertTrue(gameVisualizer.m_robotPositionX >= 0 && gameVisualizer.m_robotPositionX <= 300);
        assertTrue(gameVisualizer.m_robotPositionY >= 0 && gameVisualizer.m_robotPositionY <= 300);
    }
}
