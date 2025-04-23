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
        gameVisualizer.robot.move(10, 0, 1000); // Движение вперёд, выход за границы

        assertTrue(gameVisualizer.robot.positionX >= 0 && gameVisualizer.robot.positionX <= 600,
                "X координата должна быть в пределах [0, 600]");
        assertTrue(gameVisualizer.robot.positionY >= 0 && gameVisualizer.robot.positionY <= 600,
                "Y координата должна быть в пределах [0, 600]");
    }
}
